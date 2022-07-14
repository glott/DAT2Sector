########## MODIFY THESE VALUES ##########
center = (37.142778, -120.324722)
radius = 235.0
file_loc = "C:\\Users\\Josh\\Downloads\\NCT Video Maps.xml"

########### DO NOT EDIT BELOW ###########
header = "CENTER\t" + str(center) + "\nRADIUS\t" + str(round(float(radius), 1)) \
    + "\nFILE\t" + str(file_loc) +"\n"

import imp, os, sys, re
try:
    imp.find_module("geopy")
except ImportError:
    os.system("pip install geopy")

import geopy.distance

os.system("cls")
print(header)

file_in = open(file_loc)
file_out = open(file_loc + "_mod", 'w')

i = 0
num_lines = sum(1 for line in open(file_loc))

for line in file_in:
    if "StartLon" in line:
        lon = float(re.search('StartLon="(.*)" StartLat', line).group(1))
        lat = float(re.search('StartLat="(.*)" EndLon', line).group(1))
        coords = (lat, lon)
        if(abs(lat) > 90): continue
        
        dist = geopy.distance.geodesic(center, coords).nm

        if(dist <= radius): file_out.write(line)
    else: 
        file_out.write(line)
        
    i += 1
    if i % 100 == 0:
        sys.stdout.write("\r" + str(round(i / num_lines * 100, 1)) + "%")
        sys.stdout.flush()

file_in.close()
file_out.close()