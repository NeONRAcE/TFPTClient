import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
//todo maven, gradle, spring framework, junit, sqllite
import java.util.Scanner;

import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPClient;	

public class TFTPConnect {
	
	static TFTPClient tftp;
	
	public static void main(String[] args)
	{
		tftp = new TFTPClient();
		tftp.setDefaultTimeout(60000);
		
		try
        {
            tftp.open();
        }
		
        catch (SocketException e)
        {
            System.err.println("Error: could not open local UDP socket.");
            System.err.println(e.getMessage());
            System.exit(1);
        }
		if (receiveFile)
        {
            FileOutputStream output = null;
            File file;

            file = new File(localFilename);

            // If file exists, don't overwrite it.
            if (file.exists())
            {
                System.err.println("Error: " + localFilename + " already exists.");
                System.exit(1);
            }

            // Try to open local file for writing
            try
            {
                output = new FileOutputStream(file);
            }
            catch (IOException e)
            {
                tftp.close();
                System.err.println("Error: could not open local file for writing.");
                System.err.println(e.getMessage());
                System.exit(1);
            }

            // Try to receive remote file via TFTP
            try
            {
                tftp.receiveFile(remoteFilename, transferMode, output, hostname);
            }
            catch (UnknownHostException e)
            {
                System.err.println("Error: could not resolve hostname.");
                System.err.println(e.getMessage());
                System.exit(1);
            }
            catch (IOException e)
            {
                System.err.println(
                    "Error: I/O exception occurred while receiving file.");
                System.err.println(e.getMessage());
                System.exit(1);
            }
            finally
            {
                // Close local socket and output file
                tftp.close();
                try
                {
                    if (output != null) {
                        output.close();
                    }
                    closed = true;
                }
                catch (IOException e)
                {
                    closed = false;
                    System.err.println("Error: error closing file.");
                    System.err.println(e.getMessage());
                }
            }

            if (!closed) {
                System.exit(1);
            }

        } else {
            // We're sending a file
            FileInputStream input = null;

            // Try to open local file for reading
            try
            {
                input = new FileInputStream(localFilename);
            }
            catch (IOException e)
            {
                tftp.close();
                System.err.println("Error: could not open local file for reading.");
                System.err.println(e.getMessage());
                System.exit(1);
            }

            // Try to send local file via TFTP
            try
            {
                tftp.sendFile(remoteFilename, transferMode, input, hostname);
            }
            catch (UnknownHostException e)
            {
                System.err.println("Error: could not resolve hostname.");
                System.err.println(e.getMessage());
                System.exit(1);
            }
            catch (IOException e)
            {
                System.err.println(
                    "Error: I/O exception occurred while sending file.");
                System.err.println(e.getMessage());
                System.exit(1);
            }
            finally
            {
                // Close local socket and input file
                tftp.close();
                try
                {
                    if (input != null) {
                        input.close();
                    }
                    closed = true;
                }
                catch (IOException e)
                {
                    closed = false;
                    System.err.println("Error: error closing file.");
                    System.err.println(e.getMessage());
                }
            }

            if (!closed) {
                System.exit(1);
            }

        }
	}
}
