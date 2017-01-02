import hudson.security.SecurityRealm
import org.jenkinsci.plugins.GithubSecurityRealm

import hudson.security.AuthorizationStrategy
import org.jenkinsci.plugins.GithubAuthorizationStrategy

/*
 * Change security realm
 */
boolean saveJenkinsInstance = false;
String githubWebUri = '{{ github_web_uri }}'
String githubApiUri = '{{ github_api_uri }}'
String clientID = '{{ github_client_id }}'
String clientSecret = '{{ github_client_secret }}'
String oauthScopes = 'read:org,user:email'
SecurityRealm github_realm = new GithubSecurityRealm(githubWebUri, githubApiUri, clientID, clientSecret, oauthScopes)
//check for equality, no need to modify the runtime if no settings changed
if(!github_realm.equals(Jenkins.instance.getSecurityRealm())) {
    Jenkins.instance.setSecurityRealm(github_realm)
    saveJenkinsInstance = true
}

/*
 * Change authorization strategy
 */
//Permissions are ordered similar to web UI
//Admin User Names
String adminUserNames = '{{ github_jenkins_admins|join(",") }}'
//Participant in Organization
String organizationNames = '{{ github_organization_names|join(",") }}'
//Use Github repository permissions
boolean useRepositoryPermissions = true
//Grant READ permissions to all Authenticated Users
boolean authenticatedUserReadPermission = false
//Grant CREATE Job permissions to all Authenticated Users
boolean authenticatedUserCreateJobPermission = false
//Grant READ permissions for /github-webhook
boolean allowGithubWebHookPermission = true
//Grant READ permissions for /cc.xml
boolean allowCcTrayPermission = false
//Grant READ permissions for Anonymous Users
boolean allowAnonymousReadPermission = true
//Grant ViewStatus permissions for Anonymous Users
boolean allowAnonymousJobStatusPermission = true

AuthorizationStrategy github_authorization = new GithubAuthorizationStrategy(adminUserNames,
    authenticatedUserReadPermission,
    useRepositoryPermissions,
    authenticatedUserCreateJobPermission,
    organizationNames,
    allowGithubWebHookPermission,
    allowCcTrayPermission,
    allowAnonymousReadPermission,
    allowAnonymousJobStatusPermission)

//check for equality, no need to modify the runtime if no settings changed
if(!github_authorization.equals(Jenkins.instance.getAuthorizationStrategy())) {
    Jenkins.instance.setAuthorizationStrategy(github_authorization)
    saveJenkinsInstance = true
}

if (saveJenkinsInstance) {
    Jenkins.instance.save()
}
