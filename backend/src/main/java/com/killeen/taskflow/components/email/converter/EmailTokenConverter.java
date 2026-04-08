import java.util.List;

import org.mapstruct.Mapper;

import com.killeen.taskflow.components.email.model.EmailToken;

@Mapper(componentModel = "spring")
public interface EmailTokenConverter {
    EmailToken toDto(EmailTokenDb db);
    EmailTokenDb toDb(EmailToken dto);
    List<EmailToken> toDtoList(List<EmailTokenDb> dbList);
}
