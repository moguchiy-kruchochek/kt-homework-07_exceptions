package ru.netology

data class Chat(
    val chatId: Int = 0,
    val userId: Int = 0,
    val userName: String = "user",
    val messages: MutableList<Message> = mutableListOf(),
    val isDeleted: Boolean = false
)

data class Message(
    val messageId: Int = 0,
    val text: String = "text",
    var isUnread: Boolean = true,
    val isDeleted: Boolean = false,
)

class ChatService {

    private val chats = mutableListOf<Chat>()
    private var nextChatId = 1
    private var nextMessageId = 1

    fun createChat(userId: Int): Int {
        val existingChat = chats.find { it.userId == userId && !it.isDeleted }
        if (existingChat != null) return existingChat.chatId
        val newChat = Chat(
            chatId = nextChatId,
            userId = userId
        )
        chats.add(newChat)
        nextChatId++
        println("chat with user $userId created!")
        return newChat.chatId
    }

    fun deleteChat(chatId: Int): Boolean {
        val chat = chats.find { it.chatId == chatId && !it.isDeleted }
        if (chat == null) return false

        val index = chats.indexOf(chat)
        chats[index] = chat.copy(isDeleted = true)
        println("chat with user ${chat.userId} deleted!")
        return true
    }


    fun getChats(): List<Chat> {
        return chats.filter { !it.isDeleted }
    }

    fun getUnreadChatsCount(): Int {
        return chats.filter { chat -> !chat.isDeleted }
            .filter { chat -> chat.messages.any { message -> message.isUnread && !message.isDeleted } }.size
    }

    fun getLastMessages(): List<String> {
        return chats.filter { !it.isDeleted }.map { chat ->
            val notDeletedMessages = chat.messages.filter { !it.isDeleted }
            if (notDeletedMessages.isEmpty()) {
                "no messages"
            } else {
                notDeletedMessages.last().text
            }
        }
    }

    fun getMessagesByUserId(userId: Int, count: Int): List<Message> {
        val chat = chats.find { it.userId == userId && !it.isDeleted }
        if (chat == null) return emptyList()

        val unreadMessages = chat.messages.filter { it.isUnread && !it.isDeleted }.take(count)
        unreadMessages.forEach { it.isUnread = false }
        return unreadMessages
    }

    fun createMessage(userId: Int, text: String): Boolean {
        var chat = chats.find { it.userId == userId && !it.isDeleted }
        if (chat == null) {
            val newChat = Chat(
                chatId = nextChatId,
                userId = userId,
            )
            println("chat with user $userId created!")
            chats.add(newChat)
            nextChatId++
            chat = newChat
        }
        val newMessage = Message(
            messageId = nextMessageId,
            text = text
        )
        nextMessageId++
        chat.messages.add(newMessage)
        println("message '$text' to user '$userId' created!")
        return true
    }

    fun deleteMessage(chatId: Int, messageId: Int): Boolean {
        val chat = chats.find { it.chatId == chatId && !it.isDeleted }
        if (chat == null) return false

        val message = chat.messages.find { it.messageId == messageId && !it.isDeleted }
        if (message == null) return false

        val index = chat.messages.indexOf(message)
        chat.messages[index] = message.copy(isDeleted = true)
        println("message in chat '$chatId' deleted!")
        return true
    }

    fun editMessage(chatId: Int, messageId: Int, newText: String): Boolean {
        val chat = chats.find { it.chatId == chatId && !it.isDeleted }
        if (chat == null) return false

        val message = chat.messages.find { it.messageId == messageId && !it.isDeleted }
        if (message == null) return false

        val index = chat.messages.indexOf(message)
        chat.messages[index] = message.copy(text = newText)
        println("message with id '$messageId' in chat '$chatId' edited to '$newText!'")
        return true
    }

}