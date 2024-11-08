package com.example.task_management_app.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.task_management_app.model.Attachment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:/database/project/add-three-projects.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:/database/task/add-three-tasks.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:/database/attachment/add-three-attachments.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:/database/attachment/remove-all-attachments.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = "classpath:/database/task/remove-all-tasks.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = "classpath:/database/project/remove-all-projects.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class AttachmentRepositoryTest {
    private static final int FIRST_ATTACHMENT_INDEX = 0;
    private static final int SECOND_ATTACHMENT_INDEX = 1;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Test
    @DisplayName(value = "Should return two attachments for task with id = 1")
    public void findAllAttachmentsByTaskId_ReturnTwoAttachments_Ok() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Attachment attachment1 = new Attachment();
        attachment1.setId(1L);
        attachment1.setFileName("game_logic_diagram.pdf");
        attachment1.setDropboxFileId("dropbox1234");
        attachment1.setUploadDate(LocalDateTime.parse("2024-11-03 12:15:00", formatter));

        Attachment attachment2 = new Attachment();
        attachment2.setId(2L);
        attachment2.setFileName("game_rules.txt");
        attachment2.setDropboxFileId("dropbox5678");
        attachment2.setUploadDate(LocalDateTime.parse("2024-11-05 15:35:00", formatter));

        List<Attachment> expected = List.of(attachment1, attachment2);
        List<Attachment> actual = attachmentRepository.findAllByTaskId(1L);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected.get(FIRST_ATTACHMENT_INDEX).getId(),
                actual.get(FIRST_ATTACHMENT_INDEX).getId());
        assertEquals(expected.get(FIRST_ATTACHMENT_INDEX).getFileName(),
                actual.get(FIRST_ATTACHMENT_INDEX).getFileName());
        assertEquals(expected.get(FIRST_ATTACHMENT_INDEX).getDropboxFileId(),
                actual.get(FIRST_ATTACHMENT_INDEX).getDropboxFileId());
        assertEquals(expected.get(FIRST_ATTACHMENT_INDEX).getUploadDate(),
                actual.get(FIRST_ATTACHMENT_INDEX).getUploadDate());
        assertEquals(expected.get(SECOND_ATTACHMENT_INDEX).getId(),
                actual.get(SECOND_ATTACHMENT_INDEX).getId());
        assertEquals(expected.get(SECOND_ATTACHMENT_INDEX).getFileName(),
                actual.get(SECOND_ATTACHMENT_INDEX).getFileName());
        assertEquals(expected.get(SECOND_ATTACHMENT_INDEX).getDropboxFileId(),
                actual.get(SECOND_ATTACHMENT_INDEX).getDropboxFileId());
        assertEquals(expected.get(SECOND_ATTACHMENT_INDEX).getUploadDate(),
                actual.get(SECOND_ATTACHMENT_INDEX).getUploadDate());
    }

    @Test
    @DisplayName(value = "Should return only one attachment for task with id = 3")
    void findAllAttachmentsByTaskId_ReturnOneAttachment_Ok() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Attachment attachment = new Attachment();
        attachment.setId(3L);
        attachment.setFileName("bug_report_template.docx");
        attachment.setDropboxFileId("dropbox1112");
        attachment.setUploadDate(LocalDateTime.parse("2024-11-08 12:00:00", formatter));

        List<Attachment> expected = List.of(attachment);
        List<Attachment> actual = attachmentRepository.findAllByTaskId(3L);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(FIRST_ATTACHMENT_INDEX).getId(),
                actual.get(FIRST_ATTACHMENT_INDEX).getId());
        assertEquals(expected.get(FIRST_ATTACHMENT_INDEX).getFileName(),
                actual.get(FIRST_ATTACHMENT_INDEX).getFileName());
        assertEquals(expected.get(FIRST_ATTACHMENT_INDEX).getDropboxFileId(),
                actual.get(FIRST_ATTACHMENT_INDEX).getDropboxFileId());
        assertEquals(expected.get(FIRST_ATTACHMENT_INDEX).getUploadDate(),
                actual.get(FIRST_ATTACHMENT_INDEX).getUploadDate());
    }

}
