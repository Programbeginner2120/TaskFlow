package com.killeen.taskflow.components.user.repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.killeen.taskflow.components.user.converter.UserConverter;
import com.killeen.taskflow.components.user.model.User;
import com.killeen.taskflow.db.mapper.generated.UserDbMapper;
import com.killeen.taskflow.db.model.generated.UserDb;
import com.killeen.taskflow.db.model.generated.UserDbExample;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserDbMapper userDbMapper;
    private final UserConverter userConverter;

    @Transactional
    public User save(User user) {
        UserDb dbModel = userConverter.toDb(user);
        userDbMapper.insert(dbModel);
        user.setId(dbModel.getId());
        return user;
    }

    public Optional<User> findByEmail(String email) {
        UserDbExample example = new UserDbExample();
        example.createCriteria().andEmailEqualTo(email);
        List<UserDb> results = userDbMapper.selectByExample(example);
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userConverter.toDto(results.get(0)));
    }

    public Optional<User> findById(Long id) {
        UserDb dbModel = userDbMapper.selectByPrimaryKey(id);
        if (dbModel == null) {
            return Optional.empty();
        }
        return Optional.of(userConverter.toDto(dbModel));
    }

    public void setEmailVerified(Long userId, boolean verified) {
        UserDb update = new UserDb();
        update.setId(userId);
        update.setEmailVerified(verified);
        update.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        userDbMapper.updateByPrimaryKeySelective(update);
    }

    public void updatePassword(Long userId, String newPasswordHash) {
        UserDb update = new UserDb();
        update.setId(userId);
        update.setPasswordHash(newPasswordHash);
        update.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        userDbMapper.updateByPrimaryKeySelective(update);
    }
}
