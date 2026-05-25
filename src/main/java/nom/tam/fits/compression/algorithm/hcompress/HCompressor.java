package nom.tam.fits.compression.algorithm.hcompress;

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
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import nom.tam.fits.compression.algorithm.api.ICompressor;
import nom.tam.fits.compression.algorithm.quant.QuantizeProcessor.DoubleQuantCompressor;
import nom.tam.fits.compression.algorithm.quant.QuantizeProcessor.FloatQuantCompressor;
import nom.tam.util.ArrayFuncs;

/**
 * (<i>for internal use</i>) Data compressor using the HCompress algorithm.
 *
 * @param <T> The generic type of buffer that accessed the type of elements needed for the compression
 */
@SuppressWarnings("javadoc")
public abstract class HCompressor<T extends Buffer> implements ICompressor<T> {

    public static class ByteHCompressor extends HCompressor<ByteBuffer> {

        private static final long BYTE_MASK_FOR_LONG = 0xFFL;

        /**
         * HCompress of byte streams with the default scale parameter of 0 and no smoothing (lossless compression).
         *
         * @since 1.19.1
         */
        public ByteHCompressor() {
            this(new HCompressorOption());
        }

        public ByteHCompressor(HCompressorOption options) {
            super(options);
        }

        @Override
        public boolean compress(ByteBuffer buffer, ByteBuffer compressed) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void decompress(ByteBuffer compressed, ByteBuffer buffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    public static class DoubleHCompressor extends DoubleQuantCompressor {

        @SuppressWarnings("deprecation")
        public DoubleHCompressor(HCompressorQuantizeOption options) {
            super(options, new IntHCompressor((HCompressorOption) options.getCompressOption()));
        }
    }

    public static class FloatHCompressor extends FloatQuantCompressor {

        @SuppressWarnings("deprecation")
        public FloatHCompressor(HCompressorQuantizeOption options) {
            super(options, new IntHCompressor((HCompressorOption) options.getCompressOption()));
        }
    }

    public static class IntHCompressor extends HCompressor<IntBuffer> {

        /**
         * HCompress of 32-bit integer streams with the default scale parameter of 0 and no smoothing for lossless
         * compression.
         *
         * @since 1.19.1
         */
        public IntHCompressor() {
            this(new HCompressorOption());
        }

        public IntHCompressor(HCompressorOption options) {
            super(options);
        }

        @Override
        public boolean compress(IntBuffer buffer, ByteBuffer compressed) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void decompress(ByteBuffer compressed, IntBuffer buffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    public static class ShortHCompressor extends HCompressor<ShortBuffer> {

        /**
         * HCompress of 16-bit integer streams with the default scale parameter of 0 (lossless compression).
         *
         * @since 1.19.1
         */
        public ShortHCompressor() {
            this(new HCompressorOption());
        }

        public ShortHCompressor(HCompressorOption options) {
            super(options);
        }

        @Override
        public boolean compress(ShortBuffer buffer, ByteBuffer compressed) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void decompress(ByteBuffer compressed, ShortBuffer buffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    private final HCompress compress;

    private final HDecompress decompress;

    private final HCompressorOption options;

    public HCompressor(HCompressorOption options) {
        this.options = options;
        compress = new HCompress();
        decompress = new HDecompress();
    }

    private HCompress compress() {
        return compress;
    }

    protected void compress(long[] longArray, ByteBuffer compressed) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private HDecompress decompress() {
        return decompress;
    }

    protected void decompress(ByteBuffer compressed, long[] aa) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
