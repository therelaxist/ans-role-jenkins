import jenkins.model.*

def inst = Jenkins.getInstance()

def desc = inst.getDescriptor("hudson.plugins.git.GitSCM")

desc.setGlobalConfigName("{{ git_config.name }}")
desc.setGlobalConfigEmail("{{ git_config.email }}")

desc.save()