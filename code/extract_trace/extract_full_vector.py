import os
import subprocess

cmd = 'java -Xms6144m -Xmx7168m FrequencyProcessing '

# linux
srcpath = "./"
dstpath = "./"

# read from apps-all.txt
application_list = []
with open("apps.txt") as fin_apps:
	for line in fin_apps:
	    application_list.append(line.strip())

# Open file for writing log
# f = open("log.txt", "w")
#f.write("Starting...\n")
print("Starting...\n")

for c in application_list:
    # makedir
	if not os.path.exists(dstpath+c):
		os.makedirs(dstpath+c)
	
	# linux
	commandString = cmd+srcpath+c+'.txt '+dstpath+c+"/"+c+'_freqvector_test.csv' #> log.txt'
	print(commandString)
	'''
	#Open file for writing log
	f_log = open("log"+c+".txt", "w")
	'''

	# EXECUTE
	# b = os.popen(commandString)).read()
	#with open(os.devnull, "w") as ff:
	#subprocess.call(commandString, shell=True, stdout=ff)
	subprocess.call(commandString, shell=True)
	

	'''
	#Open file for writing
	f_log.write(b + " \n")
	f_log.close()
	'''
	print("Finished cve, " + c + " \n")
#f.close()
