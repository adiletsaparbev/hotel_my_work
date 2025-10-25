package com.hotel_proj.hotel.service;

import com.hotel_proj.hotel.models.Amenities;
import com.hotel_proj.hotel.models.Image;
import com.hotel_proj.hotel.models.Rooms;
import com.hotel_proj.hotel.models.Views;
import com.hotel_proj.hotel.repository.RoomRepository;
import com.hotel_proj.hotel.repository.AmenitieRepository;

import com.hotel_proj.hotel.repository.ViewRepository;
import jakarta.transaction.Transactional;

import com.hotel_proj.hotel.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class RoomService {

    private final ImageRepository imageRepository;
    private final RoomRepository roomRepository;
    private final AmenitieRepository amenityRepository; // <-- ДОБАВЛЕНО
    private final ViewRepository viewRepository;

    // --- КОНСТРУКТОР ИЗМЕНЕН ---
    public RoomService(RoomRepository roomRepository, ImageRepository imageRepository, AmenitieRepository amenityRepository, ViewRepository viewRepository) {
        this.roomRepository = roomRepository;
        this.imageRepository = imageRepository;
        this.amenityRepository = amenityRepository;
        this.viewRepository = viewRepository;
    }

    // Получить все комнаты
    public List<Rooms> getAllRooms() {
        return roomRepository.findAll();
    }

    // Найти комнату по ID
    public Optional<Rooms> getRoomById(Long id) {
        return roomRepository.findById(id);
    }
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    // Сохранение новой комнаты
    @Transactional
    public void saveRoom(Rooms room, List<Long> amenityIds, List<Long> viewIds, MultipartFile file1,
                         MultipartFile file2, MultipartFile file3) throws IOException {

        if (roomRepository.findByRoomNumber(room.getRoomNumber()) != null) {
            throw new IllegalArgumentException("Room with this number already exists.");
        }

        addImagesToRoom(room, file1, file2, file3);
        Rooms roomFromDb = roomRepository.save(room);

        if (viewIds != null && !viewIds.isEmpty()){
            Set<Views> views = viewIds.stream()
                    .map(viewId -> viewRepository.findById(viewId)
                            .orElseThrow(() -> new IllegalArgumentException("Вид на окно не найдено: " + viewIds)))
                    .collect(Collectors.toSet());
            roomFromDb.setViews(views);
        } else {
            roomFromDb.getViews().clear();
        }
        // Обновляем удобства
        if (amenityIds != null && !amenityIds.isEmpty()) {
            Set<Amenities> amenities = amenityIds.stream()
                    .map(amenityId -> amenityRepository.findById(amenityId)
                            .orElseThrow(() -> new IllegalArgumentException("Удобство не найдено: " + amenityId)))
                    .collect(Collectors.toSet());
            roomFromDb.setAmenities(amenities);
        } else {
            roomFromDb.getAmenities().clear(); // Если ничего не выбрано, очищаем список удобств
        }

        if (!roomFromDb.getImages().isEmpty()) {
            roomFromDb.setPreviewImageId(roomFromDb.getImages().get(0).getId());
            roomRepository.save(roomFromDb);
        }
    }

    // 🔥 Обновление комнаты

    @Transactional
    public void updateRoom(Long id, Rooms updatedRoomData, List<Long> amenityIds, List<Long> viewIds, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        Rooms existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Комната не найдена"));

        // Обновляем основные поля
        existingRoom.setRoomNumber(updatedRoomData.getRoomNumber());
        existingRoom.setFloor(updatedRoomData.getFloor());
        existingRoom.setCapacity(updatedRoomData.getCapacity());
        existingRoom.setPriceNight(updatedRoomData.getPriceNight());
        existingRoom.setDescription(updatedRoomData.getDescription());

        // Обновляем удобства
        if (amenityIds != null && !amenityIds.isEmpty()) {
            Set<Amenities> amenities = amenityIds.stream()
                    .map(amenityId -> amenityRepository.findById(amenityId)
                            .orElseThrow(() -> new IllegalArgumentException("Удобство не найдено: " + amenityId)))
                    .collect(Collectors.toSet());
            existingRoom.setAmenities(amenities);
        } else {
            existingRoom.getAmenities().clear(); // Если ничего не выбрано, очищаем список удобств
        }
        if (viewIds != null && !viewIds.isEmpty()){
            Set<Views> views = viewIds.stream()
                    .map(viewId -> viewRepository.findById(viewId)
                            .orElseThrow(() -> new IllegalArgumentException("Вид на окно не найдено: " + viewIds)))
                    .collect(Collectors.toSet());
            existingRoom.setViews(views);
        } else {
            existingRoom.getViews().clear();
        }

        // Добавляем новые картинки, если они загружены
        addImagesToRoom(existingRoom, file1, file2, file3);
        roomRepository.save(existingRoom);
    }

    // Вынесли работу с файлами в отдельный метод
    private void addImagesToRoom(Rooms room,
                                 MultipartFile file1,
                                 MultipartFile file2,
                                 MultipartFile file3) throws IOException {
        if (file1 != null && !file1.isEmpty()) {
            Image image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            room.addImageRoom(image1);
        }
        if (file2 != null && !file2.isEmpty()) {
            Image image2 = toImageEntity(file2);
            room.addImageRoom(image2);
        }
        if (file3 != null && !file3.isEmpty()) {
            Image image3 = toImageEntity(file3);
            room.addImageRoom(image3);
        }
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }


    // Удаление картинки
    public void deleteImage(Long roomId, Long imageId) {
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Комната не найдена"));

        room.getImages().removeIf(img -> img.getId().equals(imageId));
        imageRepository.deleteById(imageId);

        // если удалили preview → назначим новое
        if (room.getPreviewImageId() != null && room.getPreviewImageId().equals(imageId)) {
            if (!room.getImages().isEmpty()) {
                room.setPreviewImageId(room.getImages().get(0).getId());
            } else {
                room.setPreviewImageId(null);
            }
        }

        roomRepository.save(room);
    }

    // Замена картинки
    public void replaceImage(Long roomId, Long imageId, MultipartFile newImage) throws IOException {
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Комната не найдена"));

        room.getImages().removeIf(img -> img.getId().equals(imageId));
        imageRepository.deleteById(imageId);

        Image imageEntity = toImageEntity(newImage);
        room.addImageRoom(imageEntity);

        // если заменяли preview → назначаем новую
        if (room.getPreviewImageId() != null && room.getPreviewImageId().equals(imageId)) {
            room.setPreviewImageId(imageEntity.getId());
        }

        roomRepository.save(room);
    }


}
