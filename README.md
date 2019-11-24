# NikoBot-Plugin-Reply
###### tags: `NikoBot`

## Function
While detecting the key words, bot will send message or image to discord channel.
## Installation
Put this plugin's JAR file into ```plugins/``` folder.
## Config formate
This plugin will generate a ```reply/``` folder.
The folder contains ```cmd/```,```img/```,```msg/```, and ```OnTag.yml```.  

### cmd/ 
Put key world file.
#### formate
```
<img|msg>
<folder name>
<condition>
<break|continue>
```
### img/
Contain many folders, each of them contain at least one image. When key words is matched, bot will select targeted folder and send randomly one of the images in that folder. 

### msg/
Contain many folders, each of them contain at least one text file. When key words is matched, bot will select targeted folder and send randomly one of text of the files in that folder. 

## Examples

### Send image
If you want to detect ```Hello Niko``` or ```Hello niko``` and make your bot send something, you can set config as follows.

#### cmd/hello.yml
```
img
hello
Hello&&(Niko||niko)
break
```
#### img/hello/
Put some images in this folder.  
Bot will select one to send.

### Send message
If you want to detect ```Hello Niko``` or ```Hello niko``` and make your bot send something, you can set config as follows.

#### cmd/hello.yml
```
msg
hello
Hello&&(Niko||niko)
break
```
#### msg/hello/
Put some text files in this folder.  
Bot will select one to send.
