/*
 * #%L
 * nom.tam FITS library
 * %%
 * Copyright (C) 1996 - 2024 nom-tam-fits
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

// What do we use in here?
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import nom.tam.fits.FitsFactory;
import nom.tam.fits.utilities.FitsCheckSum;

/**
 * For reading FITS files through an {@link InputStream}.
 * <p>
 * Testing and timing routines are provided in the nom.tam.util.test.BufferedFileTester class.
 * <p>
 * Prior versions under the old <code>BufferedDataInputStream</code>:
 * <ul>
 * <li>Version 1.1 -- October 12, 2000: Fixed handling of EOF to return partially read arrays when EOF is detected</li>
 * <li>Version 1.2 -- July 20, 2009: Added handling of very large Object arrays. Additional work is required to handle
 * very large arrays generally.</li>
 * </ul>
 * <p>
 * Version 2.0 -- October 30, 2021: Completely overhauled, with new name and hierarchy. Performance is 2-4 times better
 * than before (Attila Kovacs)
 *
 * @see FitsInputStream
 * @see FitsFile
 */
@SuppressWarnings("deprecation")
public class FitsInputStream extends ArrayInputStream implements ArrayDataInput {

    /**
     * buffer for checksum calculation
     */
    private ByteBuffer check;

    /**
     * aggregated checksum
     */
    private long sum;

    /**
     * the input, as accessible via the <code>DataInput</code> interface
     */
    private DataInput data;

    /**
     * Create a BufferedInputStream based on a input stream with a specified buffer size.
     *
     * @param i         the input stream to use for reading.
     * @param bufLength the buffer length to use.
     */
    public FitsInputStream(InputStream i, int bufLength) {
        super(i, bufLength);
        data = new DataInputStream(this);
        check = ByteBuffer.allocate(FitsFactory.FITS_BLOCK_SIZE);
        check.limit(check.capacity());
        setDecoder(new FitsDecoder(this));
    }

    /**
     * Create a BufferedInputStream based on an input stream.
     *
     * @param i the input stream to use for reading.
     */
    public FitsInputStream(InputStream i) {
        this(i, FitsIO.DEFAULT_BUFFER_SIZE);
    }

    @Override
    protected FitsDecoder getDecoder() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public synchronized int read() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public synchronized int read(byte[] b, int from, int len) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private void aggregate() {
        sum = FitsCheckSum.sumOf(sum, FitsCheckSum.checksum(check));
        check.position(0);
    }

    /**
     * (<i>for internal use</i>) Returns the aggregated checksum for this stream since the last call to this method, or
     * else since instantiation. Checksums for a block of data thus may be obtained by calling this method both before
     * and after reading the data block -- the first call resets the checksum and the block checksum is returned on the
     * second call. The checksummed block must be a multiple of 2880 bytes (the FITS block size) for the result to be
     * valid.
     *
     * @return the aggregated checksum since the last call to this method, or else since instantiation, on an integer
     *             number of FITS blocks read in the meantime.
     *
     * @since  1.18.1
     */
    public final long nextChecksum() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int read(boolean[] b, int start, int length) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int read(Boolean[] b, int start, int length) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int read(char[] c, int start, int length) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int read(short[] s, int start, int length) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int read(int[] i, int start, int length) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int read(long[] l, int start, int length) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int read(float[] f, int start, int length) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int read(double[] d, int start, int length) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * This routine provides efficient reading of arrays of any primitive type. It is an error to invoke this method
     * with an object that is not an array of some primitive type. Note that there is no corresponding capability to
     * writePrimitiveArray in BufferedDataOutputStream to read in an array of Strings.
     *
     * @return                 number of bytes read.
     *
     * @param      o           The object to be read. It must be an array of a primitive type, or an array of Object's.
     *
     * @throws     IOException if the underlying read operation fails
     *
     * @deprecated             use {@link #readLArray(Object)} instead
     */
    @Deprecated
    public final int readPrimitiveArray(Object o) throws IOException {
        return (int) readLArray(o);
    }

    @Override
    public synchronized long skip(long n) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int skipBytes(int n) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void skipAllBytes(long toSkip) throws EOFException, IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public boolean readBoolean() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int readUnsignedByte() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public byte readByte() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public char readChar() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int readUnsignedShort() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public short readShort() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public int readInt() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public long readLong() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public float readFloat() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public double readDouble() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public String readUTF() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public final String readLine() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
