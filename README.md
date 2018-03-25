# PortScanner
A Java program to scan the Internet to your hearts content.



# How to use

1. Download from the releases page.
2. Upload to your server (DO NOT RUN THIS ON YOUR HOME COMPUTER).
 -> Not because I'm encouraging you to be a skid, because this can get rather intensive if you do not have certain requirements. 
 -> If you don't have a server, pick one up from [Vultr](http://www.vultr.com/?ref=6914293-3B) and get $20 free credit (4 months of hosting on the lowest package)!.
3. Make sure you have Java installed on said server. **This program requires Java 8. Use it. It wont harm you.**
4. Run `java -jar PortScanner.jar`
 - This will initalize the program, and begin to ask you for desired port range to scan.
 - Looking to scan only one IP address? That's fine. Just use <DESIRED-IP-ADDRESS>/32.
 - This program works of CIDR's, and if you input an incorrect one, it'll let you know. For more info on CIDR's, check out here; http://www.ipaddressguide.com/cidr
 
 
 
# Advanced usage.

1. Obviously, I couldn't leave out my Minecraft related friends, so there is an option to scan for MC servers too.
 - Just append `--mcscan` to the command usage, like shown above.
 
2. And for those who like to live dangerously. This program currently uses the first 8 threads available, if this isn't good enough for you, that's fine.
 - There is an option called `--superbeefymachine`, that will use a cached thread pool. **This will use every thread available. And I only advise you use this on clean servers**
 - This will max your CPU, however will reduce the speeds significantly. (I scanned a /24 in less than a second if I remember correctly).
 
# Pull requests and that

If you wish to contribute to this project, that's great.
Just please be aware of the license that is specified for this project, and that all the code remains the intellectual property of the original developer.
If you wish to add libraries, please also make sure you include the licenses in the source or else I will reject your requests. 

**Plagirism is theft**

# Terms of usage.

I don't set a lot of terms, other than:
 - Don't abuse this. Don't use it for illegal stuff. I made this for fun, and I'm beginning to post more on GitHub. Don't abuse it.
 - I am not responsible for anything you do, or break with this application. You accept all responsibility for the application when you download it.
 - If your ISP (or server's ISP) hates you over this, again, not my fault. All responsibility lies on you.
 - You are _not permitted under any circumstances to sell this application_. Makes me sad :C
 
 # Libraries used
 - Apache Commons, Networking. https://commons.apache.org/proper/commons-net/
 - Google Gson. https://github.com/google/gson
 - jopt-simple. https://pholser.github.io/jopt-simple/
 
 Please find, read and understand their respective licenses.
 
 
 # And enjoy. 
