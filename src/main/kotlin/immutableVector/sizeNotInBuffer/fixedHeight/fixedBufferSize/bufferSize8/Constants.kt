package immutableVector.sizeNotInBuffer.fixedHeight.fixedBufferSize.bufferSize8

const val MAX_BUFFER_SIZE = 8
const val LOG_MAX_BUFFER_SIZE = 3
const val MAX_BUFFER_SIZE_MINUS_ONE = MAX_BUFFER_SIZE - 1
const val SHIFT_START = 30  /// 31 - LOG_MAX_BUFFER_SIZE <= SHIFT_START < 31 && SHIFT_START % LOG_MAX_BUFFER_SIZE == 0
const val REST_HEIGHT = SHIFT_START / LOG_MAX_BUFFER_SIZE + 1