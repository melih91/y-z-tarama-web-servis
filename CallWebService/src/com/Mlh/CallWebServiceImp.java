package com.Mlh;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

@WebService(endpointInterface = "com.Mlh.callwebservice")
@SOAPBinding(style = Style.RPC)
public class CallWebServiceImp implements callwebservice {

	@Override
	public String my() {
		String melih = "Hello World";
		return melih;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Mlh.callwebservice#checkUserImage(java.lang.String, byte[])
	 */
	@Override
	public int checkUserImage(@WebParam(name="tcNumber") String tc, @WebParam(name="ImageByte") byte[] imageBytes) {

		String tmp = tc.toString();

		if (tmp.length() != 11) {
			System.out.println("Tc 11 haneli degil!");
			return 0;
		}

		if (Long.valueOf(tmp) % 2 != 0) {
			System.out.println("Hatalý tc kimlik numarasý girdiniz!");
			return 0;
		}

		String user = "kafein";
		String password = "kafein";
		String host = "192.168.34.172";
		int port = 22;

		String remoteFile = "/home/kafein/Desktop/Melih/";
		String remoteFile2 = "/home/kafein/Desktop/Melih/" + tc + "/sonuc.txt";

		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			System.out.println("Establishing Connection...");
			session.connect();
			System.out.println("Connection established.");
			System.out.println("Crating SFTP Channel.");
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			System.out.println("SFTP Channel created.");

			String pathfortc = "/home/kafein/Desktop/Melih/" + tc;
			Path id = Paths.get(pathfortc);
			
			SftpATTRS attrs=null;
			try {
			    attrs = sftpChannel.stat(pathfortc);
			} catch (Exception e) {
			    System.out.println(pathfortc+" not found");
			}
			
			if (attrs == null) {
				
				sftpChannel.cd(remoteFile);
				
				sftpChannel.mkdir(tc);

				sftpChannel.cd(tc);
				InputStream myInputStream = new ByteArrayInputStream(imageBytes);
				sftpChannel.put(myInputStream, "resim.jpg");
			} else {

				sftpChannel.cd(pathfortc);
				InputStream myInputStream = new ByteArrayInputStream(imageBytes);
				sftpChannel.put(myInputStream, "gelen.jpg");

				ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
		        InputStream in=null;
				try {
					in = channelExec.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        channelExec.setCommand("python3 /home/kafein/Desktop/Melih/face.py " +tc);
		        channelExec.connect();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		        String line;
		        int index = 0;

		        try {
					while ((line = reader.readLine()) != null)
					{
					    System.out.println(++index + " : " + line);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		        int exitStatus = channelExec.getExitStatus();
		        channelExec.disconnect();
			}

			try {
				Thread.sleep(2000); // 2000 milliseconds is one second.
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			InputStream stream = sftpChannel.get(remoteFile2);
			int sonuc = 0;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));
				String line;
				try {
					while ((line = br.readLine()) != null) {
						System.out.println("sonuc " + line);
						sonuc = Integer.valueOf(line);
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// read from br
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			System.out.println("Received file: " + remoteFile);
			return 1;
		}
			catch (  JSchException | SftpException ex) {
			System.err.println(ex);
			return 0;
		}
	}
	
}
