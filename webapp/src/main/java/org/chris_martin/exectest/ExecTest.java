package org.chris_martin.exectest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.nio.channels.Channels.newChannel;
import static java.util.Arrays.asList;

public class ExecTest extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        File dir = File.createTempFile("foo", null);
        File foo = new File(dir, "foo");

        try {

            {
                dir.delete();
                dir.mkdir();
                InputStream in = ExecTest.class.getResourceAsStream("foo");
                FileOutputStream out = new FileOutputStream(foo);
                copy(in, out);
                in.close();
                out.close();
            }

            OutputStream out = resp.getOutputStream();

            {
                Process process = new ProcessBuilder()
                    .directory(dir)
                    .command(asList("chmod", "+x", "foo"))
                    .redirectErrorStream(true)
                    .start();

                InputStream in = process.getInputStream();
                copy(in, out);
                in.close();
            }

            {
                Process process = new ProcessBuilder()
                    .directory(dir)
                    .command(asList("./foo", new Date().toString()))
                    .redirectErrorStream(true)
                    .start();

                InputStream in = process.getInputStream();
                copy(in, out);
                in.close();
            }

            out.close();

        } finally {
            foo.delete();
            dir.delete();
        }

    }

    void copy(InputStream in, OutputStream out) throws IOException {
        copy(newChannel(in), newChannel(out));
    }

    void copy(ReadableByteChannel in, WritableByteChannel out) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (in.read(buffer) != -1) {
            buffer.flip();
            out.write(buffer);
            buffer.compact();
        }
        buffer.flip();
        while (buffer.hasRemaining()) {
            out.write(buffer);
        }
    }

}
