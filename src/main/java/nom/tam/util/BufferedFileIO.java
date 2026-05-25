/*
 * #%L
 * nom.tam FITS library
 * %%
 * Copyright (C) 2004 - 2024 nom-tam-fits
 * %%
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * #L%
 */
package nom.tam.util;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Basic file input/output with efficient internal buffering.
 *
 * @author Attila Kovacs
 *
 * @since  1.16
 */
class BufferedFileIO implements InputReader, OutputWriter, Flushable, Closeable {

    /**
     * Bit mask for a single byte
     */
    protected static final int BYTE_MASK = 0xFF;

    /**
     * The underlying unbuffered random access file IO
     */
    private final RandomAccessFileIO file;

    /**
     * The file position at which the buffer begins
     */
    private long startOfBuf;

    /**
     * The buffer
     */
    private final byte[] buf;

    /**
     * Pointer to the next byte to read/write
     */
    private int offset;

    /**
     * The last legal element in the buffer
     */
    private int end;

    /**
     * Whether the buffer has been modified locally, so it needs to be written back to stream before discarding.
     */
    private boolean isModified;

    /**
     * Whether the current position is beyond the current ennd-of-file
     */
    private boolean writeAhead;

    /**
     * Instantiates a new buffered random access file with the specified IO mode and buffer size. This class offers up
     * to 2+ orders of magnitude superior performance over {@link RandomAccessFile} when repeatedly reading or writing
     * chunks of data at consecutive locations
     *
     * @param  f           the file
     * @param  mode        the access mode, such as "rw" (see {@link RandomAccessFile} for more info).
     * @param  bufferSize  the size of the buffer in bytes
     *
     * @throws IOException if there was an IO error getting the required access to the file.
     */
    @SuppressWarnings("resource")
    BufferedFileIO(File f, String mode, int bufferSize) throws IOException {
        this(new RandomFileIO(f, mode), bufferSize);
    }

    /**
     * Instantiates a new buffered random access file with the provided RandomAccessFileIO and buffer size. This allows
     * implementors to provide alternate RandomAccessFile-like implementations, such as network accessed (byte range
     * request) files.
     *
     * @param f          the RandomAccessFileIO implementation
     * @param bufferSize the size of the buffer in bytes
     */
    BufferedFileIO(RandomAccessFileIO f, int bufferSize) {
        file = f;
        buf = new byte[bufferSize];
        startOfBuf = 0;
        offset = 0;
        end = 0;
        isModified = false;
        writeAhead = false;
    }

    /**
     * Sets a new position in the file for subsequent reading or writing.
     *
     * @param  newPos      the new byte offset from the beginning of the file. It may be beyond the current end of the
     *                         file, for example for writing more data after some 'gap'.
     *
     * @throws IOException if the position is negative or cannot be set.
     */
    public final synchronized void seek(long newPos) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Get the channel associated with this file. Note that this returns the channel of the associated RandomAccessFile.
     * Note that since the BufferedFile buffers the I/O's to the underlying file, the offset of the channel may be
     * different from the offset of the BufferedFile. This is different for a RandomAccessFile where the offsets are
     * guaranteed to be the same.
     *
     * @return the file channel
     */
    public final FileChannel getChannel() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Get the file descriptor associated with this stream. Note that this returns the file descriptor of the associated
     * RandomAccessFile.
     *
     * @return             the file descriptor
     *
     * @throws IOException if the descriptor could not be accessed.
     */
    public final FileDescriptor getFD() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Gets the current read/write position in the file.
     *
     * @return the current byte offset from the beginning of the file.
     */
    public final synchronized long getFilePointer() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the number of bytes that can still be read from the file before the end is reached.
     *
     * @return             the number of bytes left to read.
     *
     * @throws IOException if there was an IO error.
     */
    private synchronized long getRemaining() throws IOException {
        long n = file.length() - getFilePointer();
        return n > 0 ? n : 0L;
    }

    /**
     * Checks if there is more that can be read from this file. If so, it ensures that the next byte is buffered.
     *
     * @return             <code>true</code> if there is more that can be read from the file (and from the buffer!), or
     *                         else <code>false</code>.
     *
     * @throws IOException if there was an IO error accessing the file.
     */
    private synchronized boolean makeAvailable() throws IOException {
        if (offset < end) {
            return true;
        }
        if (getRemaining() <= 0) {
            return false;
        }
        // Buffer as much as we can.
        seek(getFilePointer());
        end = file.read(buf, 0, buf.length);
        return end > 0;
    }

    /**
     * Checks if there are the required number of bytes available to read from this file.
     *
     * @param  need        the number of bytes we need.
     *
     * @return             <code>true</code> if the needed number of bytes can be read from the file or else
     *                         <code>false</code>.
     *
     * @throws IOException if there was an IO error accessing the file.
     */
    public final synchronized boolean hasAvailable(int need) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the current length of the file.
     *
     * @return             the current length of the file.
     *
     * @throws IOException if the operation failed
     */
    public final synchronized long length() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Sets the length of the file. This method calls the method of the same name in {@link RandomAccessFileIO}.
     *
     * @param  newLength   The number of bytes at which the file is set.
     *
     * @throws IOException if the resizing of the underlying stream fails
     */
    public synchronized void setLength(long newLength) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Positions the file pointer to match the current buffer pointer.
     *
     * @throws IOException if there was an IO error
     */
    private synchronized void matchBufferPos() throws IOException {
        file.position(getFilePointer());
    }

    /**
     * Positions the buffer pointer to match the current file pointer.
     *
     * @throws IOException if there was an IO error
     */
    private synchronized void matchFilePos() throws IOException {
        seek(file.position());
    }

    @Override
    public synchronized void close() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Moves the buffer to the current read/write position.
     *
     * @throws IOException if there was an IO error writing the prior data back to the file.
     */
    private synchronized void moveBuffer() throws IOException {
        seek(getFilePointer());
    }

    @Override
    public synchronized void flush() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public final synchronized void write(int b) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public final synchronized int read() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public final synchronized void write(byte[] b, int from, int len) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public final synchronized int read(byte[] b, int from, int len) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Reads bytes to completely fill the supplied buffer. If not enough bytes are avaialable in the file to fully fill
     * the buffer, an {@link EOFException} will be thrown.
     *
     * @param  b            the buffer
     *
     * @throws EOFException if already at the end of file.
     * @throws IOException  if there was an IO error before the buffer could be fully populated.
     */
    public final synchronized void readFully(byte[] b) throws EOFException, IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Reads bytes to fill the supplied buffer with the requested number of bytes from the given starting buffer index.
     * If not enough bytes are avaialable in the file to deliver the reqauested number of bytes the buffer, an
     * {@link EOFException} will be thrown.
     *
     * @param  b            the buffer
     * @param  off          the buffer index at which to start reading data
     * @param  len          the total number of bytes to read.
     *
     * @throws EOFException if already at the end of file.
     * @throws IOException  if there was an IO error before the requested number of bytes could all be read.
     */
    public synchronized void readFully(byte[] b, int off, int len) throws EOFException, IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Same as {@link RandomAccessFile#readUTF()}.
     *
     * @return             a string
     *
     * @throws IOException if there was an IO error while reading from the file.
     */
    public final synchronized String readUTF() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Same as {@link RandomAccessFile#writeUTF(String)}
     *
     * @param  s           a string
     *
     * @throws IOException if there was an IO error while writing to the file.
     */
    public final synchronized void writeUTF(String s) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Moves the file pointer by a number of bytes from its current position.
     *
     * @param  n           the number of byter to move. Negative values are allowed and result in moving the pointer
     *                         backward.
     *
     * @return             the actual number of bytes that the pointer moved, which may be fewer than requested if the
     *                         file boundary was reached.
     *
     * @throws IOException if there was an IO error.
     */
    public final synchronized long skip(long n) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Read as many bytes into a byte array as possible. The number of bytes read may be fewer than the size of the
     * array, for example because the end-of-file is reached during the read.
     *
     * @param  b           the byte buffer to fill with data from the file.
     *
     * @return             the number of bytes actually read.
     *
     * @throws IOException if there was an IO error while reading, other than the end-of-file.
     */
    public final synchronized int read(byte[] b) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Writes the contents of a byte array into the file.
     *
     * @param  b           the byte buffer to write into the file.
     *
     * @throws IOException if there was an IO error while writing to the file...
     */
    public final synchronized void write(byte[] b) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Default implementation of the RandomAccessFileIO interface.
     */
    static final class RandomFileIO extends RandomAccessFile implements RandomAccessFileIO {

        RandomFileIO(File file, String mode) throws FileNotFoundException {
            super(file, mode);
        }

        @Override
        public long position() throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void position(long n) throws IOException {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }
}
