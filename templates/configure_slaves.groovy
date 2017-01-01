import jenkins.model.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import hudson.plugins.sshslaves.*;

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

Jenkins.instance.getNode("{{ jenkins_slave }}");