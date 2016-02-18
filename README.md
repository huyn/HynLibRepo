#This is a demo for uploading lib to jcenter
主要参考http://rocko.xyz/2015/02/02/%E4%BD%BF%E7%94%A8Gradle%E5%8F%91%E5%B8%83%E9%A1%B9%E7%9B%AE%E5%88%B0JCenter%E4%BB%93%E5%BA%93/

然而root下的build.gradle这个文件的配置：
classpath 'com.github.dcendents:android-maven-plugin:1.2'
会导致一个错误：
http://stackoverflow.com/questions/32220047/gradle-error-after-studio-update

解决方案也在此提问下给出：
classpath 'com.github.dcendents:android-maven-gradle-plugin:1.3'

#发布
两个命令：
gradle install
gradle bintrayUpload
依次在Android Studio Terminal执行即可