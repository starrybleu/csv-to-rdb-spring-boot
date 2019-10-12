package io.github.starrybleu;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class MigrateService {

    private final GroupRepository groupRepository;
    private final LabelRepository labelRepository;
    private final PanelNumbersChunkRepository panelNumbersChunkRepository;

    @Value("classpath:groups_exported.csv")
    Resource groupsExportedCsv;

    @Value("classpath:labels_exported.csv")
    Resource labelsExportedCsv;

    @Value("classpath:label_chunks_exported_remove_index.csv")
    Resource chunkExportedCsv;

    public MigrateService(GroupRepository groupRepository, LabelRepository labelRepository, PanelNumbersChunkRepository panelNumbersChunkRepository) {
        this.groupRepository = groupRepository;
        this.labelRepository = labelRepository;
        this.panelNumbersChunkRepository = panelNumbersChunkRepository;
    }

    @Transactional
    PanelNumbersChunk migrateLabelDataFromCsvs() {
        loadAndSaveGroups();
        loadAndSaveLabels();
        List<PanelNumbersChunk> chunksFromCsv = loadAndSaveChunks();
        return chunksFromCsv.get(2);
    }

    @Transactional
    void loadAndSaveGroups() {
        List<LabelGroup> groupsFromCsv = loadGroups();
        groupsFromCsv.forEach(LabelGroup::convertTimeStampToLocalDateTime);
        log.info("groups.size() : {}", groupsFromCsv.size());
        log.info("groups..get(1) : {}", groupsFromCsv.get(1));
        groupRepository.saveAll(groupsFromCsv);
        log.info("Saving groups ok.");
    }

    private List<LabelGroup> loadGroups() {
        return loadObjectList(LabelGroup.class, groupsExportedCsv);
    }

    @Transactional
    void loadAndSaveLabels() {
        List<Label> labelsFromCsv = loadLabels();
        labelsFromCsv.forEach(Label::convertTimeStampToLocalDateTime);
        log.info("labelsFromCsv.size() : {}", labelsFromCsv.size());
        log.info("labelsFromCsv.get(1) : {}", labelsFromCsv.get(1));
        labelRepository.saveAll(labelsFromCsv);
        log.info("Saving labels ok.");
    }

    private List<Label> loadLabels() {
        return loadObjectList(Label.class, labelsExportedCsv);
    }

    @Transactional
    List<PanelNumbersChunk> loadAndSaveChunks() {
        List<PanelNumbersChunk> chunksFromCsv = loadChunks();
        chunksFromCsv.forEach(PanelNumbersChunk::adjustPanelsWhenEmpty);
        log.info("chunksFromCsv.size() : {}", chunksFromCsv.size());
        log.info("chunksFromCsv.size().get(2) : {}", chunksFromCsv.get(2));
        panelNumbersChunkRepository.saveAll(chunksFromCsv);
        log.info("Saving chunks ok.");
        return chunksFromCsv;
    }

    private List<PanelNumbersChunk> loadChunks() {
        return loadObjectList(PanelNumbersChunk.class, chunkExportedCsv);
    }

    private <T> List<T> loadObjectList(Class<T> type, Resource csvResource) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            File file = csvResource.getFile();
            MappingIterator<T> readValues = mapper.readerFor(type)
                    .with(bootstrapSchema)
                    .readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            log.error("Error occurred while loading object list from file " + csvResource.getFilename(), e);
            return Collections.emptyList();
        }
    }

}
