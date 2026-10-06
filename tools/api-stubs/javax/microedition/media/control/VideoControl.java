package javax.microedition.media.control;
import javax.microedition.media.Control;
import javax.microedition.media.MediaException;
/** Compile-only MMAPI signature stub. Never package in a MIDlet. */
public interface VideoControl extends Control {
    int USE_DIRECT_VIDEO = 1;
    Object initDisplayMode(int mode, Object arg);
    void setVisible(boolean visible);
    void setDisplayLocation(int x, int y);
    void setDisplaySize(int width, int height) throws MediaException;
    void setDisplayFullScreen(boolean fullScreen) throws MediaException;
    byte[] getSnapshot(String imageType) throws MediaException;
}
