package reisetech.studentmanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
    info = @Info(
        title = "受講生管理システム API",
        version = "1.0.0",
        description = "受講生（Student）とコース（Course）を管理するための REST API です。",
        contact = @Contact(
            name = "StudentManagement Team",
            email = "support@example.com"
        ),
        license = @License(
            name = "Apache-2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "ローカル開発環境"),
        @Server(url = "https://api.example.com", description = "本番環境（例）")
    }
)
@SpringBootApplication
public class StudentManagementApplication {


  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);

    //　実行ログの文字化け対策
    System.setOut(
        new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
    System.setErr(new PrintStream(
        new FileOutputStream(FileDescriptor.err), true, StandardCharsets.UTF_8));

  }


}