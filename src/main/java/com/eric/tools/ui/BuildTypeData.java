package com.eric.tools.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.reverse;

/**
 * @ClassName: BuildTypeData
 * @Description:
 * @Author: Eric
 * @Date: 2019/4/21 0021
 * @Email: xiao_cui_vip@163.com
 */
public final class BuildTypeData {

    static final int MAX_BUILD_SIZE_TO_CACHE = 10;

    private final LinkedList<BuildData> _lastBuilds = new LinkedList<>(Lists.newArrayList());

    private final String _id;
    private final String _name;
    private final String _projectId;
    private final String _projectName;

    private String _aliasName;

    private boolean _queued;

    public BuildTypeData( final String id, final String name, final String projectId, final String projectName ) {
        _id = id;
        _name = name;
        _projectId = projectId;
        _projectName = projectName;
    }

    public String getId( ) {
        return _id;
    }

    public String getName( ) {
        return _name;
    }

    public String getProjectName( ) {
        return _projectName;
    }

    public String getProjectId( ) {
        return _projectId;
    }

    public String getAliasName( ) {
        return _aliasName;
    }

    public void setAliasName( final String aliasName ) {
        _aliasName = aliasName;
    }

    public boolean hasRunningBuild( ) {
        return getLastBuild( BuildState.running ).isPresent( );
    }

    public Optional<BuildData> getLastBuild( final BuildState state ) {
        return getBuilds( ).stream( )
                .filter( build -> build.getState( ) == state )
                .filter( build -> build.getStatus( ) != BuildStatus.UNKNOWN )
                .findFirst( );
    }

    public Optional<BuildData> getOldestBuild( final BuildState state ) {
        return reverse( getBuilds( ) ).stream( )
                .filter( build -> build.getState( ) == state )
                .filter( build -> build.getStatus( ) != BuildStatus.UNKNOWN )
                .findFirst( );
    }

    public List<BuildData> getLastBuilds( final BuildState state, final int count ) {
        return getBuilds( ).stream( )
                .filter( build -> build.getState( ) == state )
                .filter( build -> build.getStatus( ) != BuildStatus.UNKNOWN )
                .limit( count )
                .collect( Collectors.toList( ) );
    }

    public final Optional<BuildData> getBuildById( final int id ) {
        return getBuilds( ).stream( ).filter( b -> b.getId( ) == id ).findFirst( );
    }

    public synchronized boolean isQueued( ) {
        return _queued;
    }

    public synchronized void setQueued( final boolean queued ) {
        _queued = queued;
    }

    public synchronized void registerBuild( final BuildData build ) {
        _lastBuilds.removeIf( ( b -> b.getId( ) == build.getId( ) ) );

        _lastBuilds.addFirst( build );
        _lastBuilds.sort( ( o1, o2 ) -> -Integer.compare( o1.getId( ), o2.getId( ) ) );
        if ( _lastBuilds.size( ) > MAX_BUILD_SIZE_TO_CACHE )
            _lastBuilds.removeLast( );
    }

    synchronized List<BuildData> getBuilds( ) {
        return ImmutableList.copyOf( _lastBuilds );
    }

}
