package cc.l89669.nonupdate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LoggingLevel {

    MUTE(0),
    BLOCKED(1),
    DETAILED(2),
    VERBOSE(3);

    private final @Getter int weight;
    
    public boolean contains(LoggingLevel level) {
        return this.weight >= level.weight;
    }

}
