package com.game.nim.common;

import com.game.nim.domain.service.StakeRepository;
import com.game.nim.domain.service.PlayService;
import com.game.nim.usecase.in.Move;
import com.game.nim.usecase.in.Stake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class GameConfiguration {

    private static Logger logger = LoggerFactory.getLogger(GameConfiguration.class);

    @Value("${expert.mode:false}")
    private boolean expertMode;

    @Value("${stick.amount:-1}")
    private long stickAmount;

    @Bean
    public Move move(Stake stake) {
        logger.info("Expert mode: {}", expertMode);
        return new PlayService(stake, expertMode);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Stake state() {
        logger.info("Stake amount override: {}", stickAmount);
        if (stickAmount > 0) {
            return new StakeRepository(stickAmount);
        }
        return new StakeRepository();
    }

}
