import jenkins.model.*
// import hudson.model.*
// import org.jenkinsci.main.modules.cli.auth.ssh.*

// User adminUser = User.current()
// UserPropertyImpl adminUserProperty = adminUser.getProperty(UserPropertyImpl.class);
// if (adminUserProperty == null)
//     adminUserProperty = new UserPropertyImpl("{{ jenkins_admin.authorized_key }}")
// else
//     adminUserProperty.authorizedKeys = "{{ jenkins_admin.authorized_key }}"
// adminUser.addProperty(adminUserProperty)
// adminUser.save()

def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()
jenkinsLocationConfiguration.setAdminAddress("{{ jenkins_admin.name }} <{{ jenkins_admin.email }}>")
jenkinsLocationConfiguration.setUrl("{{ jenkins_uri }}")
jenkinsLocationConfiguration.save()
