/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.robol.shiftscheduler.backend;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeMapped;
import com.sun.jna.PointerType;
import java.io.File;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author robol
 */
public class SystemUtilities {
    
    private static final String APP_NAME = "ShiftScheduler";
    
    /**
     * This function can be used to find a app-specific place when data
     * can be stored. 
     * 
     * It will run differently on the different operatin systems, in a way
     * that a safe location for (non-visible) user-data can be stored. 
     * 
     * On GNU/Linux systems this is isually ~/.local/share/AppName/
     * 
     * Note that this function will create the directory if it does not
     * exists, making sure that is always usable. 
     * 
     * @return A String with the absolute path of the directory. 
     */
    public static String getDataDir() {
        String OS = System.getProperty("os.name").toLowerCase();
        String dataDirPath = System.getProperty("user.home") + 
                File.separator + APP_NAME;
        
        // Detect the operating system and operate accordingly
        if (OS.contains("win")) {
            dataDirPath = getDataDirWindows();
        }
        else if (OS.contains("mac")) {
            return null;
        }
        else if (OS.contains("nux")) {
            dataDirPath = getDataDirLinux();
        }
        
        File dataDirFile = new File(dataDirPath);
        
        if (! dataDirFile.exists()) {
            dataDirFile.mkdirs();
        }
        else if (! dataDirFile.isDirectory()) {
            throw new RuntimeException("Data directory " + dataDirPath + 
                    " is not a directory");
        }
        
        return dataDirFile.getAbsolutePath();
    }
    
    private static String getDataDirLinux() {
        String xdg_data_dir = System.getenv("XDG_DATA_DIR");
        
        if (xdg_data_dir == null) {
            xdg_data_dir = System.getProperty("user.home") + 
                    File.separator + ".local" + 
                    File.separator + "share";
        }
        
        return xdg_data_dir + File.separator + APP_NAME;
    }
    
    /**
     * Freely inspired by http://stackoverflow.com/questions/585534/what-is-the-best-way-to-find-the-users-home-directory-in-java
     * @return 
     */
    private static String getDataDirWindows () {
        if (com.sun.jna.Platform.isWindows()) {
            HWND hwndOwner = null;
            int nFolder = Shell32.CSIDL_LOCAL_APPDATA;
            HANDLE hToken = null;
            int dwFlags = Shell32.SHGFP_TYPE_CURRENT;
            char[] pszPath = new char[Shell32.MAX_PATH];
            int hResult = Shell32.INSTANCE.SHGetFolderPath(hwndOwner, nFolder,
                    hToken, dwFlags, pszPath);
            if (Shell32.S_OK == hResult) {
                String path = new String(pszPath);
                int len = path.indexOf('\0');
                path = path.substring(0, len);
                return path + "\\" + APP_NAME;
            } else {
                System.err.println("Error retrieve data path: " + hResult);
            }            
        }
        
        return null;
    }
    
    private static final Map<String, Object> OPTIONS = new HashMap<String, Object>();
    static {
        OPTIONS.put(Library.OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
        OPTIONS.put(Library.OPTION_FUNCTION_MAPPER,
                W32APIFunctionMapper.UNICODE);
    }

    static class HANDLE extends PointerType implements NativeMapped {
    }

    static class HWND extends HANDLE {
    }    
    
    static interface Shell32 extends Library {
        public static final int MAX_PATH = 260;
        public static final int CSIDL_LOCAL_APPDATA = 0x001c;
        public static final int SHGFP_TYPE_CURRENT = 0;
        public static final int SHGFP_TYPE_DEFAULT = 1;
        public static final int S_OK = 0;

        static Shell32 INSTANCE = (Shell32) Native.loadLibrary("shell32",
                Shell32.class, OPTIONS);

        /**
         * see http://msdn.microsoft.com/en-us/library/bb762181(VS.85).aspx
         * 
         * HRESULT SHGetFolderPath( HWND hwndOwner, int nFolder, HANDLE hToken,
         * DWORD dwFlags, LPTSTR pszPath);
         */
        public int SHGetFolderPath(HWND hwndOwner, int nFolder, HANDLE hToken,
                int dwFlags, char[] pszPath);
    }
    
}
