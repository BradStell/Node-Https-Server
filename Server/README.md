#  Node-Http-Server  

### Install Node.js  
You must install node.js  
You can download it here: https://nodejs.org/en/  

### Node Modules Needed  
Open cmd.exe and navigate to the Server directory  
Then type the below commands:  
		npm install mongodb  
		npm install mongoose  

### Environment   
Need java 8 JDK installed, you can download it here http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html  
Need to include java's bin folder in your path  
	Instructions:  
		* Click windows key and type "change environment variables" and choose "Edit system environment variables" from the list (not edit environment variables for your account)  
		* Near bottom of the windows click "Environment Variables..." button  
		* In new window look in bottom section titled "System variables"  
		* On left side look under Variable section for "Path"  
		* click on "Path" and then click "Edit..." button  
			* Windows 10 -  
				* Click "New" button  
				* Add full path to java bin directory  
					* Example: on my machine its C:\Program Files\Java\jdk1.8.0_45\bin  
			* Other versions -  
				* hit right arrow key on keyboard  
				* add a ";" (colon) and then the path  
				* Example: ;C:\Program Files\Java\jdk1.8.0_45\bin  
		* Test this by opening a command prompt window and typing "java"  
			* If you get "'java' is not recognized as an internal or external command, operable program or batch file"   
				then repeat the above directions again  
			* If you get a list of how to use the java program then you have done it correctly  

### Additional Info
##### ( Already done for you now )  
You need to make a 'data' directory in the root of your Server folder to contain the database information. This file  
is created for you.  

Run the following command from a terminal windows 'mongod --dbpath <path to data folder>' to connect  
mongodb to your project. (This is done for you in start.bat)  

### Runing the program ###  
( start.bat does this for you now ) If for some reason this does not work on your machine:  
open cmd.exe and navigate to the data directory in the project structure  
	* type 'mongod --dbpath .'  
open another cmd.exe and type 'mongo'  
	* the collection name is 'UserCache'  
		type 'use UserCache' to see the database via the mongo terminal  
open another cmd.exe and navigate to the Server directory  
	type 'node index.js' to start the server  
open another cmd.exe and navigate to the Server/clients/http/ directory  
	* There are 4 files here, representing test clients  
	* There is a client each for the CRUD operations (create (client-post.js), read (client-get.js), update (client-put.js), delete (client-delete.js))  
	* In each of these programs there is a JSON object called 'secretMessage', in this object there is a nested object called 'restOfContent'. Edit  
		the 'name' 'username' and 'password' fields accordingly.  
	* Examples:  
		--First you need to put data in the database, do so with client-post.js  
			** client-post.js  

					var secretMessage = {
						"Username" : "Brad",
						"Password" : "12345",
						"method" : "POST",
						"restOfContent" : {
							"name" : "Gmail",			// Name of the source
							"username" : "Bookers",		// username at source
							"password" : "passssss"		// password for username
						}
					};

				You can add multiple username/password pairs for the same source, just change the username and password values and run the program again
					run the program by typing 'node client-post.js' in the terminal that is open in the Server/clients/http/ folder

		-- Now run client-get.js to see if the data was added to the database