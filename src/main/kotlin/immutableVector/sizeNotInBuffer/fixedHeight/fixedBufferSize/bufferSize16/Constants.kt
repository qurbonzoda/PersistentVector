package immutableVector.sizeNotInBuffer.fixedHeight.fixedBufferSize.bufferSize16

internal const val MAX_BUFFER_SIZE = 16
internal const val LOG_MAX_BUFFER_SIZE = 4
internal const val MAX_BUFFER_SIZE_MINUS_ONE = MAX_BUFFER_SIZE - 1
internal const val SHIFT_START = 28  /// 31 - LOG_MAX_BUFFER_SIZE <= SHIFT_START < 31 && SHIFT_START % LOG_MAX_BUFFER_SIZE == 0
internal const val REST_HEIGHT = SHIFT_START / LOG_MAX_BUFFER_SIZE + 1