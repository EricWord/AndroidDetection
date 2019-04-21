package com.eric.tools.ui;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
/**
 * @ClassName: BuildData
 * @Description:
 * @Author: Eric
 * @Date: 2019/4/21 0021
 * @Email: xiao_cui_vip@163.com
 */
public final class BuildData {

    private final int _id;
    private final BuildStatus _status;
    private final BuildState _state;
    private final int _percentageComplete;
    private final Optional<LocalDateTime> _finishedDate;
    private final Duration _timeLeft;

    public BuildData( final int id, final BuildStatus status, final BuildState state, final int percentageComplete, final Optional<LocalDateTime> finishedDate, Duration timeLeft ) {
        _id = id;
        _status = status;
        _state = state;
        _percentageComplete = percentageComplete;
        _finishedDate = finishedDate;
        _timeLeft = timeLeft;
    }

    public BuildState getState( ) {
        return _state;
    }

    public BuildStatus getStatus( ) {
        return _status;
    }

    public int getId( ) {
        return _id;
    }

    public int getPercentageComplete( ) {
        return _percentageComplete;
    }

    public Optional<LocalDateTime> getFinishedDate( ) {
        return _finishedDate;
    }

    public Duration getTimeLeft( ) {
        return _timeLeft;
    }
}
