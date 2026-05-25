package nom.tam.fits.compression.algorithm.rice;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.logging.Logger;
import nom.tam.fits.compression.algorithm.api.ICompressor;
import nom.tam.fits.compression.algorithm.quant.QuantizeProcessor.DoubleQuantCompressor;
import nom.tam.fits.compression.algorithm.quant.QuantizeProcessor.FloatQuantCompressor;
import nom.tam.util.FitsIO;
import nom.tam.util.type.ElementType;

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
/**
 * (<i>for internal use</i>) The Rice compression algorithm. The original compression was designed by Rice, Yeh, and
 * Miller the code was written by Richard White at STScI at the STScI and included (ported to c and adapted) in cfitsio
 * by William Pence, NASA/GSFC. That code was then ported to java by R. van Nieuwenhoven. Later it was massively
 * refactored to harmonize the different compression algorithms and reduce the duplicate code pieces without obscuring
 * the algorithm itself as far as possible.
 *
 * @author     Richard White
 * @author     William Pence
 * @author     Richard van Nieuwenhoven
 *
 * @param  <T> the genetic type of NIO buffer on which this compressor operates.
 */
@SuppressWarnings({ "deprecation", "javadoc" })
public abstract class RiceCompressor<T extends Buffer> implements ICompressor<T> {

    public static class ByteRiceCompressor extends RiceCompressor<ByteBuffer> {

        private ByteBuffer pixelBuffer;

        /**
         * Rice compression of byte streams with the default block size of 32.
         *
         * @since 1.19.1
         */
        public ByteRiceCompressor() {
            this(new RiceCompressOption());
        }

        public ByteRiceCompressor(RiceCompressOption option) {
            super(option);
        }

        @Override
        public boolean compress(ByteBuffer buffer, ByteBuffer writeBuffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void decompress(ByteBuffer readBuffer, ByteBuffer buffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected int nextPixel() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void nextPixel(int pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    public static class DoubleRiceCompressor extends DoubleQuantCompressor {

        public DoubleRiceCompressor(RiceQuantizeCompressOption options) throws ClassCastException {
            super(options, new IntRiceCompressor((RiceCompressOption) options.getCompressOption()));
        }
    }

    public static class FloatRiceCompressor extends FloatQuantCompressor {

        public FloatRiceCompressor(RiceQuantizeCompressOption options) throws ClassCastException {
            super(options, new IntRiceCompressor((RiceCompressOption) options.getCompressOption()));
        }
    }

    public static class IntRiceCompressor extends RiceCompressor<IntBuffer> {

        private IntBuffer pixelBuffer;

        /**
         * Rice compression of 32-bit integer streams with the default block size of 32.
         *
         * @since 1.19.1
         */
        public IntRiceCompressor() {
            this(new RiceCompressOption());
        }

        public IntRiceCompressor(RiceCompressOption option) {
            super(option);
        }

        @Override
        public boolean compress(IntBuffer buffer, ByteBuffer writeBuffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void decompress(ByteBuffer readBuffer, IntBuffer buffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected int nextPixel() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void nextPixel(int pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    public static class ShortRiceCompressor extends RiceCompressor<ShortBuffer> {

        private ShortBuffer pixelBuffer;

        /**
         * Rice compression of 16-bit integer streams with the default block size of 32.
         *
         * @since 1.19.1
         */
        public ShortRiceCompressor() {
            this(new RiceCompressOption());
        }

        public ShortRiceCompressor(RiceCompressOption option) {
            super(option);
        }

        @Override
        public boolean compress(ShortBuffer buffer, ByteBuffer writeBuffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void decompress(ByteBuffer readBuffer, ShortBuffer buffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected int nextPixel() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void nextPixel(int pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * mask to convert a "unsigned" byte to a long.
     */
    private static final long UNSIGNED_BYTE_MASK = 0xFFL;

    /**
     * mask to convert a "unsigned" short to a long.
     */
    private static final long UNSIGNED_SHORT_MASK = 0xFFFFL;

    /**
     * mask to convert a "unsigned" int to a long.
     */
    private static final long UNSIGNED_INTEGER_MASK = 0xFFFFFFFFL;

    /**
     * logger to log to.
     */
    private static final Logger LOG = Logger.getLogger(RiceCompressor.class.getName());

    private static final int BITS_OF_1_BYTE = 8;

    private static final int BITS_PER_BYTE = 8;

    private static final int BYTE_MASK = 0xff;

    private static final int FS_BITS_FOR_BYTE = 3;

    private static final int FS_BITS_FOR_INT = 5;

    private static final int FS_BITS_FOR_SHORT = 4;

    private static final int FS_MAX_FOR_BYTE = 6;

    private static final int FS_MAX_FOR_INT = 25;

    private static final int FS_MAX_FOR_SHORT = 14;

    /*
     * nonzero_count is lookup table giving number of bits in 8-bit values not including leading zeros used in
     * fits_rdecomp, fits_rdecomp_short and fits_rdecomp_byte.
     *
     * @formatter:off
     */
    private static final int[] NONZERO_COUNT = { 0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 };

    // @formatter:on
    private final int bBits;

    private final int bitsPerPixel;

    private final int blockSize;

    private final int fsBits;

    private final int fsMax;

    private RiceCompressor(RiceCompressOption option) throws UnsupportedOperationException {
        blockSize = option.getBlockSize();
        if (option.getBytePix() == ElementType.BYTE.size()) {
            fsBits = FS_BITS_FOR_BYTE;
            fsMax = FS_MAX_FOR_BYTE;
            bitsPerPixel = FitsIO.BITS_OF_1_BYTE;
        } else if (option.getBytePix() == ElementType.SHORT.size()) {
            fsBits = FS_BITS_FOR_SHORT;
            fsMax = FS_MAX_FOR_SHORT;
            bitsPerPixel = FitsIO.BITS_OF_2_BYTES;
        } else if (option.getBytePix() == ElementType.INT.size()) {
            fsBits = FS_BITS_FOR_INT;
            fsMax = FS_MAX_FOR_INT;
            bitsPerPixel = FitsIO.BITS_OF_4_BYTES;
        } else {
            throw new UnsupportedOperationException("Implemented for 1/2/4 bytes only");
        }
        /*
         * From bsize derive: FSBITS = # bits required to store FS FSMAX = maximum value for FS BBITS = bits/pixel for
         * direct coding
         */
        bBits = 1 << fsBits;
    }

    /**
     * <p>
     * undo mapping and differencing Note that some of these operations will overflow the unsigned int arithmetic --
     * that's OK, it all works out to give the right answers in the output file.
     * </p>
     * <p>
     * In java this is more complicated because of the missing unsigned integers. trying to simulate the behavior
     * </p>
     *
     * @param  lastpix the current last pix value
     * @param  diff    the difference to "add"
     *
     * @return         return the new lastpiy value
     */
    private long undoMappingAndDifferencing(long lastpix, long diff) {
        diff &= UNSIGNED_INTEGER_MASK;
        if ((diff & 1) == 0) {
            diff = diff >>> 1;
        } else {
            diff = diff >>> 1 ^ UNSIGNED_INTEGER_MASK;
        }
        lastpix = diff + lastpix & UNSIGNED_INTEGER_MASK;
        nextPixel((int) lastpix);
        return lastpix;
    }

    /**
     * compress the integer tiledImageOperation on a rise compressed byte buffer.
     *
     * @param dataLength length of the data to compress
     * @param firstPixel the value of the first pixel
     * @param buffer     the buffer to write to
     */
    protected void compress(final int dataLength, int firstPixel, BitBuffer buffer) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * decompress the readbuffer and fill the pixelarray.
     *
     * @param readBuffer input buffer
     * @param nx         the number of pixel to uncompress
     */
    protected void decompressBuffer(final ByteBuffer readBuffer, final int nx) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    protected abstract int nextPixel();

    protected abstract void nextPixel(int pixel);
}
