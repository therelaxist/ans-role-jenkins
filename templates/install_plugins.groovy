import hudson.*
import hudson.model.*
import jenkins.model.*

plugins = "{{ jenkins_plugins | join(' ') }}".split()

plugins.each({ plugin ->
    Jenkins.instance.updateCenter.getPlugin(plugin).deploy().get()
})
