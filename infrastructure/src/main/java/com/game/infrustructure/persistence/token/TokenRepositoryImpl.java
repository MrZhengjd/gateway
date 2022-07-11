package com.game.infrustructure.persistence.token;

import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import com.game.domain.repository.token.TokenRepository;
import org.springframework.stereotype.Service;

/**
 * @author zheng
 */
@Service
public class TokenRepositoryImpl implements TokenRepository {
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    @Override
    public String getToken(byte[] data) {
        return dataSerialize.deserialize(data,String.class);
    }
}
