import jenkins.model.*
import hudson.model.*
import hudson.slaves.*
import hudson.plugins.sshslaves.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*

// Mark master as exclusive build scheduler
Jenkins.instance.setNumExecutors(0)
Jenkins.instance.setMode(Node.Mode.EXCLUSIVE)

// Get the jenkins credentials store
credentials_store =
    Jenkins.instance.getExtensionList(
        'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
    )[0].getStore()

// Get jenkins master credential if already in store
if (SSHLauncher.lookupSystemCredentials('jenkins_master_credential')
      == null)
{   // Add jenkins masters' private key to the store
    credentials = new BasicSSHUserPrivateKey(
        // scope
        CredentialsScope.GLOBAL,
        // id
        "jenkins_master_credential",
        // username
        "{{ jenkins_user }}",
        // get private key from current user's ~/.ssh
        new BasicSSHUserPrivateKey.UsersPrivateKeySource(),
        // passphrase (null or empty)
        "",
        // description
        ""
    )
    credentials_store.addCredentials(Domain.global(), credentials)
}

if (Jenkins.instance.getNode("{{ jenkins_slave }}") == null)
{
    Slave slave = new DumbSlave(
        "{{ jenkins_slave }}",
        "{{ appuser[jenkins_user].home }}/jenkins-slave",
        new SSHLauncher(
            // Jenkins slave host
            "{{ jenkins_slave }}",
            // SSH port 
            22,
            // Id of credentials to use (stored above)
            "jenkins_master_credential",
            // Jvm options
            "",
            // Java path (empty = `which java`)
            "",
            // prefixStartSlaveCmd
            "",
            // suffixStartSlaveCmd
            "",
            // connection timeout
            5,
            // maxNumRetries
            0,
            // retryWaitTime
            0)
        )
    slave.setNumExecutors(
        {{ hostvars[inventory_hostname].ansible_processor_cores }}
        {# * {{ hostvars[inventory_hostname].ansible_processor_count }}
           * {{ hostvars[inventory_hostname].ansible_processor_threads_per_core }}#}
        )
    slave.setRetentionStrategy(new RetentionStrategy.Always())
    slave.setMode(Node.Mode.NORMAL)
    Jenkins.instance.addNode(slave)
}
