## Source Code ##

Checked-out latest stable code of OpenNMS. Steps followed:

#### Taking latest from GitHub

$ git clone https://github.com/OpenNMS/opennms.git  
$ cd opennms  
$ git checkout tags/opennms-22.0.1-1  

#### Creating project in GitLab

$ git clone https://gitlab.com/hitsquadtech/onms.git  
$ cd onms  
$ cp --recursive ~/gitrepos/opennms .  
$ git add *  
$ git commit --message="Initial checkin"  
$ git branch develop  
$ git chceckout develop  
$ git push origin develop  




