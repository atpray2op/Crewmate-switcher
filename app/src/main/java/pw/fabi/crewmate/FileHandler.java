package pw.fabi.crewmate;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.spi.CharsetProvider;

public class FileHandler {

    File file;

    public FileHandler openFile(String fileStr){
        file = new File(fileStr);
        return this;
    }

    public boolean replaceFile(String name, String ip, short port) {
        try {

            if(!file.exists()){
                // ok, stop
                File dirParent = new File(file.getParent());
                if(!dirParent.exists()){
                    dirParent.mkdirs();
                }

                file.createNewFile();
            }
            else{
                // Clear file
                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();
            }

            URL website = new URL("https://cdn.discordapp.com/attachments/777475900370976768/778425182246797331/regionInfo.dat");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            return  true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
