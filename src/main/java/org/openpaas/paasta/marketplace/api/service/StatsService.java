package org.openpaas.paasta.marketplace.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.openpaas.paasta.marketplace.api.domain.Instance;
import org.openpaas.paasta.marketplace.api.domain.Software;
import org.openpaas.paasta.marketplace.api.domain.Stats;
import org.openpaas.paasta.marketplace.api.domain.Stats.Term;
import org.openpaas.paasta.marketplace.api.repository.StatsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    public long countOfSwsCurrent() {
        return statsRepository.countOfSws(Software.Status.Approval);
    }

    public long countOfInstsCurrent() {
        return statsRepository.countOfInsts(Instance.Status.Approval);
    }

    public long countOfUsersCurrent() {
        return statsRepository.countOfUsers(Instance.Status.Approval);
    }

    public long countOfProvidersCurrent() {
        return statsRepository.countOfProviders(Software.Status.Approval);
    }

    public Map<String, Long> countsOfInstsProviderCurrent(List<String> idIn) {
        List<Object[]> values = statsRepository.countsOfInstsByProviderIds(Instance.Status.Approval, idIn);
        Map<String, Long> data = values.stream().collect(Collectors.toMap(v -> (String) v[0], v -> (Long) v[1]));

        return data;
    }

    public Map<String, Long> countsOfInstsUserCurrent(List<String> idIn) {
        List<Object[]> values = statsRepository.countsOfInstsByUserIds(Instance.Status.Approval, idIn);
        Map<String, Long> data = values.stream().collect(Collectors.toMap(v -> (String) v[0], v -> (Long) v[1]));

        return data;
    }

    public Map<Long, Long> countsOfInstsCurrent(List<Long> idIn) {
        List<Object[]> values = statsRepository.countsOfInsts(Instance.Status.Approval, idIn);
        Map<Long, Long> data = values.stream().collect(Collectors.toMap(v -> (Long) v[0], v -> (Long) v[1]));

        return data;
    }

    public Map<Long, Long> countsOfInstsCurrent(String providerId, List<Long> idIn) {
        List<Object[]> values = statsRepository.countsOfInsts(providerId, Instance.Status.Approval, idIn);
        Map<Long, Long> data = values.stream().collect(Collectors.toMap(v -> (Long) v[0], v -> (Long) v[1]));

        return data;
    }

    public long countOfSwsCurrent(String providerId) {
        return statsRepository.countOfSws(providerId, Software.Status.Approval);
    }

    public long countOfInstsCurrent(String providerId) {
        return statsRepository.countOfInsts(providerId, Instance.Status.Approval);
    }

    public long countOfUsersCurrent(String providerId) {
        return statsRepository.countOfUsers(providerId, Instance.Status.Approval);
    }

    public Map<String, Long> countsOfSwsProvider(int maxResults) {
        if (maxResults == -1) {
            maxResults = Integer.MAX_VALUE;
        }

        List<Object[]> values = statsRepository.countsOfSwsProvider(Software.Status.Approval,
                PageRequest.of(0, maxResults));
        Map<String, Long> data = new LinkedHashMap<>();
        for (Object[] v : values) {
            data.put((String) v[0], (Long) v[1]);
        }

        return data;
    }

    public Map<String, Long> countsOfInstsProvider(int maxResults) {
        if (maxResults == -1) {
            maxResults = Integer.MAX_VALUE;
        }

        List<Object[]> values = statsRepository.countsOfInstsProvider(Instance.Status.Approval,
                PageRequest.of(0, maxResults));
        Map<String, Long> data = new LinkedHashMap<>();
        for (Object[] v : values) {
            data.put((String) v[0], (Long) v[1]);
        }

        return data;
    }

    public Map<String, Long> countsOfInstsUser(int maxResults) {
        if (maxResults == -1) {
            maxResults = Integer.MAX_VALUE;
        }

        List<Object[]> values = statsRepository.countsOfInstsUser(Instance.Status.Approval,
                PageRequest.of(0, maxResults));
        Map<String, Long> data = new LinkedHashMap<>();
        for (Object[] v : values) {
            data.put((String) v[0], (Long) v[1]);
        }

        return data;
    }

    public Map<Long, Long> countsOfInstsCurrent(int maxResults) {
        if (maxResults == -1) {
            maxResults = Integer.MAX_VALUE;
        }

        List<Object[]> values = statsRepository.countsOfInsts(Instance.Status.Approval,
                PageRequest.of(0, maxResults));
        Map<Long, Long> data = new LinkedHashMap<>();
        for (Object[] v : values) {
            data.put((Long) v[0], (Long) v[1]);
        }

        return data;
    }

    public Map<Long, Long> countsOfInstsCurrent(String providerId, int maxResults) {
        if (maxResults == -1) {
            maxResults = Integer.MAX_VALUE;
        }

        List<Object[]> values = statsRepository.countsOfInsts(providerId, Instance.Status.Approval,
                PageRequest.of(0, maxResults));
        Map<Long, Long> data = new LinkedHashMap<>();
        for (Object[] v : values) {
            data.put((Long) v[0], (Long) v[1]);
        }

        return data;
    }

    public List<Long> countsOfInsts(List<Term> terms, boolean using) {
        List<Long> vs = new ArrayList<>();
        terms.forEach(t -> {
            long v = statsRepository.countOfInsts(t.getStart(), t.getEnd(), using);
            vs.add(v);
        });

        return vs;
    }

    public List<Long> countsOfInstsUser(String createdId, List<Term> terms, boolean using) {
        List<Long> vs = new ArrayList<>();
        terms.forEach(t -> {
            long v = statsRepository.countOfInstsUser(createdId, t.getStart(), t.getEnd(), using);
            vs.add(v);
        });

        return vs;
    }

    public Map<Long, List<Long>> countsOfInsts(List<Long> idIn, List<Term> terms, boolean using) {
        Map<Pair<Long, String>, Long> values = new HashMap<>();
        Set<Long> ids = new TreeSet<>();
        terms.forEach(t -> {
            List<Object[]> vs = statsRepository.countsOfInsts(idIn, t.getStart(), t.getEnd(), using);
            vs.forEach(v -> {
                Long id = (Long) v[0];
                Long count = (Long) v[1];
                ids.add(id);
                Pair<Long, String> pair = Pair.of(id, Stats.toString(t.getStart()));
                values.put(pair, count);
            });
        });

        Map<Long, List<Long>> datas = new HashMap<>();
        ids.forEach(id -> {
            List<Long> data = new ArrayList<>();
            terms.forEach(t -> {
                Long count = values.getOrDefault(Pair.of(id, Stats.toString(t.getStart())), 0L);
                data.add(count);
            });
            datas.put(id, data);
        });

        return datas;
    }

    public Map<Long, List<Long>> countsOfInsts(String providerId, List<Long> idIn, List<Term> terms, boolean using) {
        Map<Pair<Long, String>, Long> values = new HashMap<>();
        Set<Long> ids = new TreeSet<>();
        terms.forEach(t -> {
            List<Object[]> vs = statsRepository.countsOfInsts(providerId, idIn, t.getStart(), t.getEnd(), using);
            vs.forEach(v -> {
                Long id = (Long) v[0];
                Long count = (Long) v[1];
                ids.add(id);
                Pair<Long, String> pair = Pair.of(id, Stats.toString(t.getStart()));
                values.put(pair, count);
            });
        });

        Map<Long, List<Long>> datas = new HashMap<>();
        ids.forEach(id -> {
            List<Long> data = new ArrayList<>();
            terms.forEach(t -> {
                Long count = values.getOrDefault(Pair.of(id, Stats.toString(t.getStart())), 0L);
                data.add(count);
            });
            datas.put(id, data);
        });

        return datas;
    }

}
