package com.killeen.taskflow.components.refreshtoken.repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.killeen.taskflow.components.refreshtoken.converter.RefreshTokenConverter;
import com.killeen.taskflow.components.refreshtoken.model.RefreshToken;
import com.killeen.taskflow.db.mapper.generated.RefreshTokenDbMapper;
import com.killeen.taskflow.db.model.generated.RefreshTokenDb;
import com.killeen.taskflow.db.model.generated.RefreshTokenDbExample;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RefreshTokenDbMapper mapper;
    private final RefreshTokenConverter converter;

    public RefreshToken save(RefreshToken token) {
        RefreshTokenDb db = converter.toDb(token);
        mapper.insertSelective(db);
        token.setId(db.getId());
        return token;
    }


    public Optional<RefreshToken> findBySelector(String selector) {
        RefreshTokenDbExample example = new RefreshTokenDbExample();
        example.createCriteria().andSelectorEqualTo(selector);
        return mapper.selectByExample(example).stream()
            .findFirst()
            .map(converter::toDto);
    }

    public void deleteBySelector(String selector) {
        RefreshTokenDbExample example = new RefreshTokenDbExample();
        example.createCriteria().andSelectorEqualTo(selector);
        mapper.deleteByExample(example);
    }

    public void deleteByUserId(Long userId) {
        RefreshTokenDbExample example = new RefreshTokenDbExample();
        example.createCriteria().andUserIdEqualTo(userId);
        mapper.deleteByExample(example);
    }

    public void deleteExpired() {
        RefreshTokenDbExample example = new RefreshTokenDbExample();
        example.createCriteria().andExpiresAtLessThan(OffsetDateTime.now(ZoneOffset.UTC));
        mapper.deleteByExample(example);
    }

    // Marked deprecated as the "used at" is not used and will likely be removed later
    @Deprecated
    public void markUsed(Long id) {
        RefreshTokenDb update = new RefreshTokenDb();
        update.setId(id);
        update.setUsedAt(OffsetDateTime.now(ZoneOffset.UTC));
        mapper.updateByPrimaryKeySelective(update);
    }
    
}
