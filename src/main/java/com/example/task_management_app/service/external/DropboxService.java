package com.example.task_management_app.service.external;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Component;

@Component
public class DropboxService {
    private final DbxClientV2 client;

    public DropboxService(Dotenv dotenv) {
        String accessToken = dotenv.get("OAUTH2_ACCESS_TOKEN");
        DbxRequestConfig config = DbxRequestConfig.newBuilder(dotenv.get("APP_NAME")).build();
        this.client = new DbxClientV2(config, accessToken);
    }

    public String uploadFile(String fileName, InputStream inputStream)
            throws IOException, DbxException {
        FileMetadata metadata = client.files()
                .uploadBuilder("/" + fileName)
                .withMode(WriteMode.OVERWRITE)
                .uploadAndFinish(inputStream);
        return metadata.getId();
    }

    public String getFileLink(String fileId) throws DbxException {
        return client.sharing().createSharedLinkWithSettings(fileId).getUrl();
    }
}
