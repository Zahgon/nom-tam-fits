package nom.tam.image;

import java.io.EOFException;
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
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import nom.tam.util.ArrayDataOutput;
import nom.tam.util.ArrayFuncs;
import nom.tam.util.RandomAccess;
import nom.tam.util.type.ElementType;

/**
 * <p>
 * Standard image tiling implementation. FITS tiles are always 2-dimentional, but really images of any dimensions may be
 * covered with such tiles.
 * </p>
 * <p>
 * Modified May 2, 2000 by T. McGlynn to permit tiles that go off the edge of the image.
 * </p>
 */
public abstract class StandardImageTiler implements ImageTiler {

    /**
     * Returns the linear element offset in the image data for a given index position.
     *
     * @param  dims The dimensions of the array.
     * @param  pos  The index requested.
     *
     * @return      the offset of a given position.
     */
    public static long getOffset(int[] dims, int[] pos) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Increment the offset within the position array. Note that we never look at the last index since we copy data a
     * block at a time and not byte by byte.
     *
     * @param  start   The starting corner values.
     * @param  current The current offsets.
     * @param  lengths The desired dimensions of the subset.
     *
     * @return         <code>true</code> if the current array was changed
     */
    protected static boolean incrementPosition(int[] start, int[] current, int[] lengths) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Increment the offset within the position array. Note that we never look at the last index since we copy data a
     * block at a time and not byte by byte.
     *
     * @param  start   The starting corner values.
     * @param  current The current offsets.
     * @param  lengths The desired dimensions of the subset.
     * @param  steps   The desired number of steps to take until the next position.
     *
     * @return         <code>true</code> if the current array was changed
     */
    protected static boolean incrementPosition(int[] start, int[] current, int[] lengths, int[] steps) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private final RandomAccess randomAccessFile;

    private final long fileOffset;

    private final int[] dims;

    private final Class<?> base;

    /**
     * Create a tiler.
     *
     * @param f          The random access device from which image data may be read. This may be null if the tile
     *                       information is available from memory.
     * @param fileOffset The file offset within the RandomAccess device at which the data begins.
     * @param dims       The actual dimensions of the image.
     * @param base       The base class (should be a primitive type) of the image.
     */
    public StandardImageTiler(RandomAccess f, long fileOffset, int[] dims, Class<?> base) {
        randomAccessFile = f;
        this.fileOffset = fileOffset;
        this.dims = dims;
        this.base = base;
    }

    /**
     * File a tile segment from a file using a default value for striding.
     *
     * @param  output       The output to send data. This can be an ArrayDataOutput to stream data to and prevent memory
     *                          consumption of a tile being in memory.
     * @param  delta        The offset from the beginning of the image in bytes.
     * @param  outputOffset The index into the output array.
     * @param  segment      The number of elements to be read for this segment.
     *
     * @throws IOException  if the underlying stream failed
     */
    protected void fillFileData(Object output, long delta, int outputOffset, int segment) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * File a tile segment from a file, jumping each step number of values to the next read.
     *
     * @param  output       The output to send data. This can be an ArrayDataOutput to stream data to and prevent memory
     *                          consumption of a tile being in memory.
     * @param  delta        The offset from the beginning of the image in bytes.
     * @param  outputOffset The index into the output array.
     * @param  segment      The number of elements to be read for this segment.
     * @param  step         The number of jumps until the next read. Only works for streaming out data.
     *
     * @throws EOFException if already at the end of file / stream
     * @throws IOException  if the underlying stream failed
     */
    protected void fillFileData(Object output, long delta, int outputOffset, int segment, int step) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * File a tile segment from a file into the given stream. This will deal only with bytes to avoid having to check
     * the base type and calling a specific method. Converting the base type to a byte is a simple multiplication
     * operation anyway. Uses a default value for striding (1).
     *
     * @param  output      The output stream.
     * @param  delta       The offset from the beginning of the image in bytes.
     * @param  segment     The number of elements to be read for this segment.
     *
     * @throws IOException if the underlying stream failed
     */
    protected void fillFileData(ArrayDataOutput output, long delta, int segment) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * File a tile segment from a file into the given stream. This will deal only with bytes to avoid having to check
     * the base type and calling a specific method. Converting the base type to a byte is a simple multiplication
     * operation anyway.
     *
     * @param  output      The output stream.
     * @param  delta       The offset from the beginning of the image in bytes.
     * @param  segment     The number of elements to be read for this segment.
     * @param  step        The number of elements until the next read.
     *
     * @throws IOException if the underlying stream failed
     *
     * @since              1.18
     */
    protected void fillFileData(ArrayDataOutput output, long delta, int segment, int step) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Fill a single segment from memory. This routine is called recursively to handle multidimensional arrays. E.g., if
     * data is three-dimensional, this will recurse two levels until we get a call with a single dimensional datum. At
     * that point the appropriate data will be copied into the output. Uses a default value for striding (1).
     *
     * @param  data         The in-memory image data.
     * @param  posits       The current position for which data is requested.
     * @param  length       The size of the segments.
     * @param  output       The output tile.
     * @param  outputOffset The current offset into the output tile.
     * @param  dim          The current dimension being
     *
     * @throws IOException  If the output is a stream and there is an I/O error.
     */
    protected void fillMemData(Object data, int[] posits, int length, Object output, int outputOffset, int dim) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Fill a single segment from memory. This routine is called recursively to handle multidimensional arrays. E.g., if
     * data is three-dimensional, this will recurse two levels until we get a call with a single dimensional datum. At
     * that point the appropriate data will be copied into the output, jumping the number of step values.
     *
     * @param  data         The in-memory image data.
     * @param  posits       The current position for which data is requested.
     * @param  length       The size of the segments.
     * @param  output       The output tile.
     * @param  outputOffset The current offset into the output tile.
     * @param  dim          The current dimension being
     * @param  step         The number of jumps to the next value.
     *
     * @throws IOException  If the output is a stream and there is an I/O error.
     *
     * @since               1.18
     */
    protected void fillMemData(Object data, int[] posits, int length, Object output, int outputOffset, int dim, int step) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Fill the subset using a default value for striding.
     *
     * @param  data        The memory-resident data image. This may be null if the image is to be read from a file. This
     *                         should be a multidimensional primitive array.
     * @param  o           The tile to be filled. This is a simple primitive array, or an ArrayDataOutput instance.
     * @param  newDims     The dimensions of the full image.
     * @param  corners     The indices of the corner of the image.
     * @param  lengths     The dimensions of the subset.
     *
     * @throws IOException if the underlying stream failed
     */
    protected void fillTile(Object data, Object o, int[] newDims, int[] corners, int[] lengths) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Fill the subset, jumping each step value to the next read.
     *
     * @param  data        The memory-resident data image. This may be null if the image is to be read from a file. This
     *                         should be a multidimensional primitive array.
     * @param  o           The tile to be filled. This is a simple primitive array, or an ArrayDataOutput instance.
     * @param  newDims     The dimensions of the full image.
     * @param  corners     The indices of the corner of the image.
     * @param  lengths     The dimensions of the subset.
     * @param  steps       The number of steps to take until the next read in each axis.
     *
     * @throws IOException if the underlying stream failed
     */
    protected void fillTile(Object data, Object o, int[] newDims, int[] corners, int[] lengths, int[] steps) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public Object getCompleteImage() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * See if we can get the image data from memory. This may be overridden by other classes, notably in
     * nom.tam.fits.ImageData.
     *
     * @return the image data
     */
    protected abstract Object getMemoryImage();

    @Override
    public Object getTile(int[] corners, int[] lengths) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public Object getTile(int[] corners, int[] lengths, int[] steps) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void getTile(Object output, int[] corners, int[] lengths) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public void getTile(Object output, int[] corners, int[] lengths, int[] steps) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
