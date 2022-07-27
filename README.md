# DAT2Sector
_by Josh Glottmann_

**Version 1.3.2** - 07/27/2022

This application converts FAA .dat files to .sct2 files and .sct2 files to .xml files.

### __[Download](https://github.com/glott/DAT2Sector/blob/master/DAT2Sector.jar?raw=true)__

### DAT to SCT2

  1) Select the desired `.dat` files.

  2) Click on `Convert to SCT2`. The converted file will be saved in `Downloads`.

### SCT2 to XML

  1) Select the appropriate `.key` file.

  2) By default, the converted `.dat` file's `.sct2` file is automatically selected. Another `.sct2` file may be selected. 

  3) Click on `Convert to XML`. The converted file will be saved in a folder in `Downloads`.

### Retitle SCT2

  1) Select the appropriate `.key` file.

  2) By default, the converted `.dat` file's `.sct2` file is automatically selected. Another `.sct2` file may be selected.

  3) Click on `Retitle SCT2`. This will rename all the maps in the selected sector file based on the selected `.key` file.

  4) The converted file will be saved in a folder in `Downloads` with the suffix `_ret`.

### Merge XML

  1) After running `Convert to XML` above, the `Merge XML` button will become enabled.

  2) Click on `Merge XML` to combine all `.xml` files into one overall file. 

  3) The order in which the maps appear is based off the selected `.key` file.

  4) The combined file will be saved in a folder in `Downloads`.

### .key format

  ``nameofdatfile|SHORT NAME|LONG NAME|STARSGroup``

  e.g. ``mia001sgp|EAST OPS|MIA-001S EAST OPERATIONS ARRIVAL|A``

  e.g. ``fll011sgp|FLL E|FLL-011S EAST OPERATION``

  The `STARSGroup` parameter is optional and can be `A` or `B`.

### Removing map "border" (optional, automatic)

  1) Download [`remove_bound.py`](https://raw.githubusercontent.com/glott/DAT2Sector/master/remove_bound.py)

  2) Open the file with a text editor and set the coordinate value of `center` to the video map point of tangency. 

  3) Set the value of `range` to the desired removal range. All lines farther than this range from the point of tangency will be removed.

  4) Set the value of `file_loc` to the input file location.

  5) Save `remove_bound.py` and close the file.

  6) Double-click `remove_bound.py` to launch the script. The console window will close and output a new file once the border removal has finished. 

### Removing map "border" (optional, manual)

  1) Download and unzip [`dxf_convert.zip`](http://nav.vatsim-germany.org/files/library/public/dxf_convert.zip).

  2) Run `sct_2_dxf.exe` and follow the instructions.

  3) Edit the converted `.dxf` file as desired using a CAD software.

  4) Run `dxf_2_sct.exe` and follow the instructions.

  5) Use an advanced text editor and edit `NAME._sid_section.sct`. Make the following replacements: 

     1) Delete all text before the first map.

     2) Replace <code>&nbsp;Color_7ACI</code> to <code>&nbsp;</code>.

     3) Replace <code>STAR&nbsp;</code> to <code>&nbsp;</code>.

     4) Replace <code>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;S000.59.59.817 E099.00.00.000 S000.59.59.817 E099.00.00.000</code> to <code>&nbsp;</code>.

     5) Replace `N000.00.00.000 E000.00.00.000 N000.00.00.000 E000.00.00.000` to <code>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;S090.00.00.000 E099.00.00.000 S090.00.00.000 E099.00.00.000</code>.

  6) Replace the contents of the `[STAR]` section in the original `.sct2` file to that of the `NAME._sid_section.sct` file.