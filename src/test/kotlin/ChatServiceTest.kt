import org.junit.Test

import org.junit.Assert.*
import ru.netology.Chat
import ru.netology.ChatService

class ChatServiceTest {


    val service = ChatService()


    @Test
    fun createNonExistingChat() {
        val result = service.createChat(0)
        assertEquals(1, result)
    }

    @Test
    fun createDeletedChatOnSameUserId() {
        service.createChat(0)
        service.deleteChat(1)
        val result = service.createChat(0)

        assertEquals(2, result)
    }

    @Test
    fun createExistingChat() {
        val firstChat = service.createChat(0)
        val secondChat = service.createChat(0)
        assertEquals(firstChat, secondChat)
    }

    @Test
    fun deleteChatWithTrueResult() {
        service.createChat(0)
        val result = service.deleteChat(1)
        assertTrue("error", result)
    }

    @Test
    fun deleteChatWithFalseResult() {
        service.createChat(0)
        val result = service.deleteChat(2)
        assertFalse("error", result)
    }

    @Test
    fun deleteDeletedChat() {
        service.createChat(0)
        service.deleteChat(1)

        val result = service.deleteChat(1)
        assertFalse("error", result)
    }

    @Test
    fun getChats() {
        service.createChat(0)
        val result = service.getChats()
        assertEquals(1, result.size)
    }

    @Test
    fun getDeletedChats() {
        service.createChat(0)
        service.deleteChat(1)
        val result = service.getChats()
        assertEquals(0, result.size)
    }

    @Test
    fun getUnreadChatsCountShouldCountUnreadMessagesOnly() {
        service.createMessage(0, "text")
        val result = service.getUnreadChatsCount()
        assertEquals(1, result)
    }

    @Test
    fun getUnreadChatsCountShouldNotCountDeletedChats() {
        service.createMessage(0, "text")
        service.createMessage(1, "text2")
        service.deleteChat(1)
        val result = service.getUnreadChatsCount()
        assertEquals(1, result)
    }

    @Test
    fun getUnreadChatsCountShouldNotCountDeletedMessages() {
        service.createMessage(0, "text")
        service.createMessage(1, "text2")
        service.deleteMessage(1, 1)
        val result = service.getUnreadChatsCount()
        assertEquals(1, result)
    }

    @Test
    fun getUnreadChatsCountShouldNotCountChatsWithOnlyReadMessages() {
        service.createMessage(0, "text")
        service.createMessage(1, "text2")
        service.getMessagesByUserId(0, 10)
        val result = service.getUnreadChatsCount()
        assertEquals(1, result)
    }

    @Test
    fun getLastMessagesShouldReturnLastMessagesForEachChat() {
        service.createMessage(0, "text")
        service.createMessage(1, "text-text")
        service.createMessage(1, "text-text-text")
        val result = service.getLastMessages()
        assertTrue(result.contains("text"))
        assertTrue(result.contains("text-text-text"))
    }

    @Test
    fun getLastMessagesShouldReturnPromptForChatWithoutMessages() {
        service.createMessage(0, "text")
        service.createMessage(1, "text-text")
        service.createChat(2)
        val result = service.getLastMessages()
        assertTrue(result.contains("text"))
        assertTrue(result.contains("no messages"))
    }

    @Test
    fun getLastMessagesShouldReturnCountOfLastMessages() {
        service.createMessage(0, "text")
        service.createMessage(1, "text-text")
        val result = service.getLastMessages()
        assertEquals(2, result.size)
    }

    @Test
    fun getLastMessagesShouldCheckForDeletedChats() {
        service.createMessage(0, "text")
        service.createMessage(1, "text-text")
        service.deleteChat(2)
        val result = service.getLastMessages()
        assertEquals(1, result.size)
    }

    @Test
    fun getLastMessagesShouldCheckForNonDeletedMessage() {
        service.createMessage(0, "text")
        service.createMessage(1, "text-text")
        service.deleteMessage(1, 1)
        val result = service.getLastMessages()
        assertTrue(result.contains("no messages"))
        assertTrue(result.contains("text-text"))
    }

    @Test
    fun getMessagesByUserId() {
        service.createMessage(0, "text")
        val result = service.getMessagesByUserId(0, 1)
        assertEquals(1, result.size)
    }

    @Test
    fun getMessagesByUserIdShouldNotIncludeDeletedChats() {
        service.createMessage(0, "text")
        service.createMessage(1, "text-text")
        service.deleteChat(1)
        val result = service.getMessagesByUserId(1, 1)
        assertEquals(1, result.size)
    }

    @Test
    fun getMessagesByUserIdShouldReturnEmptyListForNonExistingUser() {
        service.createMessage(0, "text")
        service.createMessage(1, "text-text")
        val result = service.getMessagesByUserId(11, 1)
        assertTrue(result.isEmpty())
    }

    @Test
    fun getMessagesByUserIdShouldReturnRightCountOfMessages() {
        service.createMessage(0, "text")
        service.createMessage(0, "text-text")
        service.createMessage(0, "last message")
        val result = service.getMessagesByUserId(0, 2)
        assertEquals(2, result.size)
    }

    @Test
    fun getMessagesByUserIdShouldNotReturnDeletedMessages() {
        service.createMessage(0, "text")
        service.createMessage(0, "text-text")
        service.deleteMessage(1, 2)
        val result = service.getMessagesByUserId(0, 2)
        assertEquals(1, result.size)
    }

    @Test
    fun createMessage() {
        val result = service.createMessage(0, "text")
        assertTrue("error", result)
    }

    @Test
    fun deleteMessageReturnTrue() {
        service.createMessage(0, "text")
        val result = service.deleteMessage(1, 1)
        assertTrue("error", result)
    }

    @Test
    fun deleteMessageShouldReturnFalseForNonExistingChat() {
        service.createMessage(0, "text")
        val result = service.deleteMessage(10,1)
        assertFalse("error", result)
    }

    @Test
    fun deleteMessageShouldReturnFalseForDeletedChat() {
        service.createMessage(0, "text")
        service.deleteChat(1)
        val result = service.deleteMessage(1,1)
        assertFalse("error", result)
    }

    @Test
    fun deleteMessageShouldReturnFalseForDeletedMessage() {
        service.createMessage(0, "text")
        service.deleteMessage(1, 1)
        val result = service.deleteMessage(1,1)
        assertFalse("error", result)
    }

    @Test
    fun deleteMessageShouldReturnFalseForNonExistingMessage() {
        service.createMessage(0, "text")
        val result = service.deleteMessage(1,10)
        assertFalse("error", result)
    }

    @Test
    fun editMessageReturnTrue() {
        service.createMessage(0, "text")
        val result = service.editMessage(1, 1, "new text")
        assertTrue("error", result)
    }

    @Test
    fun editMessageReturnFalseForNonExistingChat() {
        service.createMessage(0, "text")
        val result = service.editMessage(10, 1, "new text")
        assertFalse("error", result)
    }

    @Test
    fun editMessageReturnFalseForDeletedChat() {
        service.createMessage(0, "text")
        service.deleteChat(1)
        val result = service.editMessage(1, 1, "new text")
        assertFalse("error", result)
    }

    @Test
    fun editMessageReturnFalseForNonExistingMessage() {
        service.createMessage(0, "text")
        val result = service.editMessage(1, 10, "new text")
        assertFalse("error", result)
    }

    @Test
    fun editMessageReturnFalseForDeletedMessage() {
        service.createMessage(0, "text")
        service.deleteMessage(1,1)
        val result = service.editMessage(1, 1, "new text")
        assertFalse("error", result)
    }
}