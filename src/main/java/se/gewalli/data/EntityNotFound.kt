package se.gewalli.data

class EntityNotFound : Exception {
    constructor() {}
    constructor(message: String?) : super(message) {}

    companion object {
        /**
         *
         */
        private const val serialVersionUID = 1615032090714212552L
    }
}