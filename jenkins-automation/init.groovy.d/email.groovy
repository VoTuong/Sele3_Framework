import jenkins.model.*
import hudson.tasks.Mailer

def jenkinsLocation = JenkinsLocationConfiguration.get()
jenkinsLocation.adminAddress = 'jenkins@example.com'
jenkinsLocation.save()

def descriptor = Jenkins.instance.getDescriptorByType(Mailer.DescriptorImpl)
descriptor.smtpHost = 'smtp.gmail.com'
descriptor.smtpPort = '587'
descriptor.useSsl = false
descriptor.useTls = true
descriptor.charset = 'UTF-8'
descriptor.authUsername = 'dtuong.vo@gmail.com'
descriptor.authPassword = 'leii pakc lofy ncoz' // Thay bằng App Password

descriptor.save()