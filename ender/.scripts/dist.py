# Libs
from time import localtime, strftime
import os
import shutil

# Get date, year and time
date = strftime("%m%d", localtime())
yr = strftime("%y", localtime())
time = strftime("%H:%M", localtime())


# Set build name
build = date
if(date[:1]=="0"):
    build = build[1:]

build = yr+"-"+build

# Main
print(" Creating build: " + build)

# Create folder with the build name
newpath = os.path.dirname("../.builds/"+build+'/')
if not os.path.exists(newpath):
	print("Creating Build Folder")
	os.makedirs(newpath)
else:
	print("Deleteing old Build Folder")
	shutil.rmtree(newpath, ignore_errors=True)
	print("Creating Build Folder")
	os.makedirs(newpath)

# Create build json
print("\nCreating build json")
j = "{\"id\": \""
j += build + "\"}"

# Create file
print("Create json file")
f = open(newpath+"/build.json", "w")
f.write(j)
f.close()

# Copy zips
print("Copy zip")
shutil.copy("../.builds/ender-windows.zip", newpath+'/ender-windows.zip')
shutil.copy("../.builds/ender-mac.zip", newpath+'/ender-mac.zip')

# Add file to zips
zipLocation = '"' + newpath + '/ender-windows.zip"';
jsnLocation = '"' + newpath + '/build.json"';
os.system("7z a -tzip " + zipLocation + " " + jsnLocation)
zipLocation = '"' + newpath + '/ender-mac.zip"';
os.system("7z a -tzip " + zipLocation + " " + jsnLocation)

# Rename zips
os.rename(newpath+'/ender-mac.zip', newpath+'/EnderOSX-'+build+'.zip')
os.rename(newpath+'/ender-windows.zip', newpath+'/EnderWindows-'+build+'.zip')
