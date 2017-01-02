import hudson.model.*
import jenkins.model.*

plugins = "{{ plugins.keys() | join(' ') }}".split()

plugins.each({ plugin ->
    Jenkins.instance.updateCenter.getPlugin(plugin).deploy().get()
})
