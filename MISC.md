Miscellaneous notes for developers
==================================

Android emulators
-----------------
Be sure to enable the GLESDynamicVersion flag to allow your emulator to access
your GPU for HW emulation.  Load the latest version of the OpenGL driver for your
video card.

For more information on setting this flag see here:  https://issuetracker.google.com/issues/139093659

See also the advancedFeatures.ini file - in my case (Windows7) it is located here:

    C:\android\sdk\emulator\lib\advancedFeatures.ini
    
with the relevant block here:

    # GLESDynamicVersion
    # This feature attempts to detect the maximum supported GLES version depending on
    # which OpenGL function pointers have been found on the GL libraries used
    # on the host system. Different platforms / hardware + video driver setups can
    # have different support.
    # For example, OS X is not known to support GLES 3.1.
    # If this feature is set to "off", the max supported GLES version is assumed to
    # be <= 2 and also depend on the system image only (some images only support ES 1).
    GLESDynamicVersion = off
    
Note that this file exists for each emulator so it is easier to override this flag in the .android
folder in your home dir.

Testing
---------------
The relevant portion of the Rajawali math tests were simply ported over.

Other references:
------------------
Twitter handle for Joey de Vries
<br>
https://twitter.com/JoeyDeVriez

dependencies:
--------------
Various Android framework libraries.

No Graphic libraries are used beyond the Android Framework.

The kind assistance of various Rajawali modules is gratefully acknowledged.