# DAT2Sector
_by Josh Glottmann_

**Version 0.2.0** - 08/05/2018

This application converts FAA .dat files to .sct2 files and .sct2 files to .xml files.
 
 ### DAT to SCT2
 
 1) Select the desired `.dat` files.
 
 2) Click on `Convert to SCT2`. The converted file will be saved in `Downloads`.
 
 ### Removing map "border" (optional)
 
 1) Download and unzip [dxf_convert.zip](http://nav.vatsim-germany.org/files/library/public/dxf_convert.zip)
 
 2) Run `sct_2_dxf.exe` and follow the instructions.
 
 3) Edit the converted `.dxf` file as desired using a CAD software.
 
 4) Run `dxf_2_sct.exe` and follow the instructions.
 
 5) Use an advanced text editor and edit `NAME._sid_section.sct`. Make the following replacements: 
 
     1) Delete all text before the first map.
     
     2) Replace ` Color_7ACI` to ` `
     
     3) Replace `STAR ` to ` `.
     
     4) Replace `                          S000.59.59.817 E099.00.00.000 S000.59.59.817 E099.00.00.000` to ` `
     
     5) Replace `N000.00.00.000 E000.00.00.000 N000.00.00.000 E000.00.00.000` to ``     S090.00.00.000 E099.00.00.000 S090.00.00.000 E099.00.00.000``
 
 6) Replace the contents of the `[STAR]` section in the original `.sct2` file to that of the `NAME._sid_section.sct` file.
  
 ### SCT2 to XML
 
 1) Select the appropriate `.key` file.
 
 2) By default, the converted `.dat` file's `.sct2` conversion will be automatically selected. Another `.sct2` file may be selected. 
 
 3) Click on `Convert to XML`. The converted file will be saved in a folder in `Downloads`.
  
__[Download](https://github.com/glott/DAT2Sector/blob/master/DAT2Sector.jar?raw=true)__