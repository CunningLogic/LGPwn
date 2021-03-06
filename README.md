LGPwn
=====

LG Root Exploit

Author:
jcase@cunninglogic.com

Special Thanks:
Juggie, for pointing out an odd binary in an update (spritebud), that lead to me finding this vulnerability.

Subject:
Race condition in Sprite Software?s backup software, installed by OEM on LG Android devices.

CVE ID:
CVE-2013-3685

Effect:
Locally exploited vulnerability with minimal device user interaction which results in executing code as the root user. Under specific circumstances, it is possible to exploit this vulnerability without the device user's knowledge

Products:
"Backup"
"spritebud"

Vendors:
Sprite Software
LG Electronics
Potentially other vendors

Affected Versions:
spritebud 1.3.24
backup 2.5.4105
Likely others versions as well

Affected Devices (Subject to firmware configuration):
LG-E971   LG Optimus G
LG-E973   LG Optimus G
LG-E975   LG Optimus G
LG-E975K  LG Optimus G
LG-E975T  LG Optimus G
LG-E976   LG Optimus G
LG-E977   LG Optimus G
LG-F100K  LG Optimus Vu
LG-F100L  LG Optimus Vu
LG-F100S  LG Optimus Vu
LG-F120K  LG Optimus Vu
LG-F120L  LG Optimus LTE Tag
LG-F120S  LG Optimus LTE Tag
LG-F160K  LG Optimus LTE 2
LG-F160L  LG Optimus LTE 2
LG-F160LV LG Optimus LTE 2
LG-F160S  LG Optimus LTE 2
LG-F180K  LG Optimus G
LG-F180L  LG Optimus G
LG-F180S  LG Optimus G
LG-F200K  LG Optimus Vu 2
LG-F200L  LG Optimus Vu 2
LG-F200S  LG Optimus Vu 2
LG-F240K  LG Optimus G Pro
LG-F240L  LG Optimus G Pro
LG-F240S  LG Optimus G Pro
LG-F260K  LG Optimus LTE 3
LG-F260L  LG Optimus LTE 3
LG-F260S  LG Optimus LTE 3
LG-L21    LG Optimus G
LG-LG870  LG (Unknown)
LG-LS860  LG Mach
LG-LS970  LG Optimus G
LG-P760   LG Optimus L9
LG-P769   LG Optimus L9
LG-P780   LG Optimus L7
LG-P875   LG Optimus F5
LG-P875h  LG Optimus F5
LG-P880   LG Optimus 4X HD
LG-P940   LG Prada
LG-SU540  LG Prada 3.0
LG-SU870  LG Optimus 3D Cube
LG-US780  LG Lollipop
Potentially other devices as well.


Product Information:

"Backup" and "spritebud" are a setting and application backup/restore system written by Sprite Software and deployed on LG Android smartphones. "Backup" is the end user front end app, and "spritebud" is the service that preforms the backup and restore functions.


Details:

The "spritebud" daemon is started by the init scripts and runs as the root user. Listening on a unix socket, the daemon accepts instructions from the "Backup" app. Using a crafted backup, we can write to, change permission and change ownership of any file, being that "spritebud" is running under the root user.


The crafted backup contains restore data for our exploiting application, "com.cunninglogic.lgpwn". The data includes a 50mb dummy file (a) used to increase our exploit window, su binary (b), a script (c) to install su, and a text file (d) containing the path to our script. All files are owned by the application, and are world write/read/execute. All files are restored in alphabetical order. The entire backup, after compress, is approximately 2mb. The structure of this backup is as follows:


drwxrwxrwx u0_a114  u0_a114           2013-05-28 20:13 files


./files:
- -rwxr-xr-x u0_a114  u0_a114  52428800 2013-05-22 20:06 a
- -rwxr-xr-x u0_a114  u0_a114     91992 2013-05-22 20:07 b
- -rwxr-xr-x u0_a114  u0_a114       251 2013-05-22 20:12 c
- -rwxr-xr-x u0_a114  u0_a114        42 2013-05-22 20:07 d


Prior to restoration, our exploit app runs, watches the process and waits. During restoration, the spritebud daemon first creates the files directory, then sets it's permission and owner. Next it decompresses and restores the "a" file, our 50mb dummy files. During the restoration of "a", our exploit application has time to symlink "d", our text file containing the full path to our script (c), to /sys/kernel/uevent_helper. Upon restoration of file "d", our path is written to uevent_helper. When a hotplug even occurs (which occur every few seconds), the path contained in uevent_helper is execute by the kernel and our script (c) is executed and installs the su binary (b).
