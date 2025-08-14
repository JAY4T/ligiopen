package com.jabulani.ligiopen.dto.player;

import com.jabulani.ligiopen.entity.player.Player;
import com.jabulani.ligiopen.entity.player.ClubMembership;
import com.jabulani.ligiopen.entity.player.PlayerTransfer;
import com.jabulani.ligiopen.entity.player.ClubInvitation;
import com.jabulani.ligiopen.service.file.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PlayerMapper {

    private static final Logger logger = LoggerFactory.getLogger(PlayerMapper.class);

    private final FileUploadService fileUploadService;

    @Autowired
    public PlayerMapper(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    public PlayerDto toPlayerDto(Player player) {
        if (player == null) {
            return null;
        }

        // Get current club membership
        Optional<ClubMembership> currentMembership = player.getClubMemberships().stream()
                .filter(ClubMembership::getIsActive)
                .findFirst();

        // Calculate age
        int age = calculateAge(player.getDateOfBirth());

        // Get career start date
        LocalDate careerStart = player.getClubMemberships().stream()
                .map(ClubMembership::getJoinedDate)
                .min(LocalDate::compareTo)
                .orElse(null);

        return PlayerDto.builder()
                .id(player.getId())
                .firstName(player.getFirstName())
                .lastName(player.getLastName())
                .fullName(player.getFirstName() + " " + player.getLastName())
                .nickname(player.getNickname())
                .dateOfBirth(player.getDateOfBirth())
                .age(age)
                .placeOfBirth(player.getPlaceOfBirth())
                .nationality(player.getNationality())
                .secondNationality(player.getSecondNationality())
                .heightCm(player.getHeightCm())
                .weightKg(player.getWeightKg())
                .preferredFoot(player.getPreferredFoot())
                .primaryPosition(player.getPrimaryPosition())
                .secondaryPositions(player.getSecondaryPositions())
                .mainPhotoUrl(getPhotoUrl(player.getMainPhotoId()))
                .photoUrls(getPhotoUrls(player.getPhotosIds()))
                .bio(player.getBio())
                .phoneNumber(player.getPhoneNumber())
                .email(player.getEmail())
                .emergencyContactName(player.getEmergencyContactName())
                .emergencyContactPhone(player.getEmergencyContactPhone())
                .emergencyContactRelationship(player.getEmergencyContactRelationship())
                .idNumber(player.getIdNumber())
                .passportNumber(player.getPassportNumber())
                .fkfRegistrationNumber(player.getFkfRegistrationNumber())
                .marketValue(player.getMarketValue())
                .marketValueFormatted(formatMoney(player.getMarketValue()))
                .isActive(player.getIsActive())
                .createdAt(player.getCreatedAt())
                .updatedAt(player.getUpdatedAt())
                // Current club information
                .currentClubId(currentMembership.map(cm -> cm.getClub().getId()).orElse(null))
                .currentClubName(currentMembership.map(cm -> cm.getClub().getName()).orElse(null))
                .currentClubLogo(currentMembership.map(cm -> getPhotoUrl(cm.getClub().getLogoFileId())).orElse(null))
                .currentJerseyNumber(currentMembership.map(ClubMembership::getJerseyNumber).orElse(null))
                .currentPosition(currentMembership.map(ClubMembership::getPosition).orElse(null))
                .isCaptain(currentMembership.map(ClubMembership::getIsCaptain).orElse(false))
                .isViceCaptain(currentMembership.map(ClubMembership::getIsViceCaptain).orElse(false))
                .joinedCurrentClubDate(currentMembership.map(ClubMembership::getJoinedDate).orElse(null))
                .membershipStatus(currentMembership.map(cm -> cm.getMembershipStatus().toString()).orElse(null))
                .contractType(currentMembership.map(cm -> cm.getContractType().toString()).orElse(null))
                .contractEndDate(currentMembership.map(ClubMembership::getContractEndDate).orElse(null))
                // Career statistics
                .totalClubs(player.getClubMemberships().size())
                .totalTransfers(player.getTransfers().size())
                .careerStartDate(careerStart)
                .status(determinePlayerStatus(player, currentMembership))
                .availableForTransfer(isAvailableForTransfer(player))
                // Additional computed fields
                .displayName(getDisplayName(player))
                .heightFormatted(formatHeight(player.getHeightCm()))
                .weightFormatted(formatWeight(player.getWeightKg()))
                .ageGroup(determineAgeGroup(age))
                .experienceLevel(determineExperienceLevel(player))
                .build();
    }

    public PlayerDto toBasicPlayerDto(Player player) {
        if (player == null) {
            return null;
        }

        // Get current club membership
        Optional<ClubMembership> currentMembership = player.getClubMemberships().stream()
                .filter(ClubMembership::getIsActive)
                .findFirst();

        return PlayerDto.builder()
                .id(player.getId())
                .firstName(player.getFirstName())
                .lastName(player.getLastName())
                .fullName(player.getFirstName() + " " + player.getLastName())
                .nickname(player.getNickname())
                .age(calculateAge(player.getDateOfBirth()))
                .nationality(player.getNationality())
                .primaryPosition(player.getPrimaryPosition())
                .mainPhotoUrl(getPhotoUrl(player.getMainPhotoId()))
                .marketValue(player.getMarketValue())
                .marketValueFormatted(formatMoney(player.getMarketValue()))
                .isActive(player.getIsActive())
                // Current club information
                .currentClubId(currentMembership.map(cm -> cm.getClub().getId()).orElse(null))
                .currentClubName(currentMembership.map(cm -> cm.getClub().getName()).orElse(null))
                .currentJerseyNumber(currentMembership.map(ClubMembership::getJerseyNumber).orElse(null))
                .status(determinePlayerStatus(player, currentMembership))
                .displayName(getDisplayName(player))
                .ageGroup(determineAgeGroup(calculateAge(player.getDateOfBirth())))
                .build();
    }

    public ClubMembershipDto toClubMembershipDto(ClubMembership membership) {
        if (membership == null) {
            return null;
        }

        Player player = membership.getPlayer();
        LocalDate joinedDate = membership.getJoinedDate();
        LocalDate leftDate = membership.getLeftDate();
        long durationInDays = joinedDate != null ? 
            ChronoUnit.DAYS.between(joinedDate, leftDate != null ? leftDate : LocalDate.now()) : 0;

        return ClubMembershipDto.builder()
                .id(membership.getId())
                .playerId(player.getId())
                .playerName(player.getFirstName() + " " + player.getLastName())
                .playerPhotoUrl(getPhotoUrl(player.getMainPhotoId()))
                .clubId(membership.getClub().getId())
                .clubName(membership.getClub().getName())
                .clubLogo(getPhotoUrl(membership.getClub().getLogoFileId()))
                .jerseyNumber(membership.getJerseyNumber())
                .position(membership.getPosition())
                .isActive(membership.getIsActive())
                .joinedDate(joinedDate)
                .leftDate(leftDate)
                .isCaptain(membership.getIsCaptain())
                .isViceCaptain(membership.getIsViceCaptain())
                .contractType(membership.getContractType())
                .contractStartDate(membership.getContractStartDate())
                .contractEndDate(membership.getContractEndDate())
                .weeklyWage(membership.getWeeklyWage())
                .weeklyWageFormatted(formatMoney(membership.getWeeklyWage()))
                .signingFee(membership.getSigningFee())
                .signingFeeFormatted(formatMoney(membership.getSigningFee()))
                .membershipStatus(membership.getMembershipStatus())
                .statusDescription(membership.getMembershipStatus().toString())
                .durationInDays(durationInDays)
                .durationFormatted(formatDuration(durationInDays))
                .isCurrentMembership(membership.getIsActive())
                .contractExpiringSoon(isContractExpiringSoon(membership.getContractEndDate()))
                .roleDescription(getRoleDescription(membership))
                .build();
    }

    public PlayerTransferDto toPlayerTransferDto(PlayerTransfer transfer) {
        if (transfer == null) {
            return null;
        }

        Player player = transfer.getPlayer();
        LocalDate transferDate = transfer.getTransferDate();
        LocalDate announcementDate = transfer.getAnnouncementDate();
        long daysInProcess = (announcementDate != null && transferDate != null) ?
            ChronoUnit.DAYS.between(announcementDate, transferDate) : 0;

        return PlayerTransferDto.builder()
                .id(transfer.getId())
                .playerId(player.getId())
                .playerName(player.getFirstName() + " " + player.getLastName())
                .playerPhotoUrl(getPhotoUrl(player.getMainPhotoId()))
                .fromClubId(transfer.getFromClub() != null ? transfer.getFromClub().getId() : null)
                .fromClubName(transfer.getFromClub() != null ? transfer.getFromClub().getName() : null)
                .fromClubLogo(transfer.getFromClub() != null ? getPhotoUrl(transfer.getFromClub().getLogoFileId()) : null)
                .toClubId(transfer.getToClub() != null ? transfer.getToClub().getId() : null)
                .toClubName(transfer.getToClub() != null ? transfer.getToClub().getName() : null)
                .toClubLogo(transfer.getToClub() != null ? getPhotoUrl(transfer.getToClub().getLogoFileId()) : null)
                .transferDate(transferDate)
                .transferFee(transfer.getTransferFee())
                .transferFeeFormatted(formatMoney(transfer.getTransferFee()))
                .agentFee(transfer.getAgentFee())
                .agentFeeFormatted(formatMoney(transfer.getAgentFee()))
                .contractUntil(transfer.getContractUntil())
                .transferType(transfer.getTransferType())
                .transferTypeDescription(transfer.getTransferType().toString())
                .transferStatus(transfer.getTransferStatus())
                .transferStatusDescription(transfer.getTransferStatus().toString())
                .transferWindow(transfer.getTransferWindow())
                .announcementDate(announcementDate)
                .medicalCompleted(transfer.getMedicalCompleted())
                .contractSigned(transfer.getContractSigned())
                .notes(transfer.getNotes())
                .transferDescription(buildTransferDescription(transfer))
                .isPermanent(transfer.getTransferType() == PlayerTransfer.TransferType.PERMANENT)
                .isLoan(transfer.getTransferType() == PlayerTransfer.TransferType.LOAN)
                .isFreeTransfer(transfer.getTransferType() == PlayerTransfer.TransferType.FREE_TRANSFER)
                .statusBadgeColor(getTransferStatusBadgeColor(transfer.getTransferStatus()))
                .daysInProcess(daysInProcess)
                .isRecentTransfer(transferDate != null && ChronoUnit.DAYS.between(transferDate, LocalDate.now()) <= 30)
                .build();
    }

    public ClubInvitationDto toClubInvitationDto(ClubInvitation invitation) {
        if (invitation == null) {
            return null;
        }

        Player player = invitation.getPlayer();
        LocalDateTime expiresAt = invitation.getExpiresAt();
        boolean isExpired = expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
        long hoursUntilExpiry = expiresAt != null ? 
            ChronoUnit.HOURS.between(LocalDateTime.now(), expiresAt) : 0;

        return ClubInvitationDto.builder()
                .id(invitation.getId())
                .clubId(invitation.getClub().getId())
                .clubName(invitation.getClub().getName())
                .clubLogo(getPhotoUrl(invitation.getClub().getLogoFileId()))
                .playerId(player.getId())
                .playerName(player.getFirstName() + " " + player.getLastName())
                .playerPhotoUrl(getPhotoUrl(player.getMainPhotoId()))
                .invitedByUserId(invitation.getInvitedBy().getId())
                .invitedByUserName(invitation.getInvitedBy().getFirstName() + " " + invitation.getInvitedBy().getLastName())
                .status(invitation.getStatus())
                .statusDescription(invitation.getStatus().toString())
                .invitedAt(invitation.getInvitedAt())
                .respondedAt(invitation.getRespondedAt())
                .expiresAt(expiresAt)
                .message(invitation.getMessage())
                .proposedJerseyNumber(invitation.getProposedJerseyNumber())
                .proposedPosition(invitation.getProposedPosition())
                .proposedPositionName(invitation.getProposedPosition() != null ? invitation.getProposedPosition().toString() : null)
                .proposedWage(invitation.getProposedWage())
                .proposedWageFormatted(formatMoney(invitation.getProposedWage()))
                .isExpired(isExpired)
                .isPending(invitation.getStatus() == ClubInvitation.InvitationStatus.PENDING)
                .isAccepted(invitation.getStatus() == ClubInvitation.InvitationStatus.ACCEPTED)
                .isRejected(invitation.getStatus() == ClubInvitation.InvitationStatus.REJECTED)
                .isCancelled(invitation.getStatus() == ClubInvitation.InvitationStatus.CANCELLED)
                .hoursUntilExpiry(Math.max(0, hoursUntilExpiry))
                .timeUntilExpiry(formatTimeUntilExpiry(hoursUntilExpiry))
                .statusBadgeColor(getInvitationStatusBadgeColor(invitation.getStatus()))
                .invitationSummary(buildInvitationSummary(invitation))
                .canRespond(canRespondToInvitation(invitation))
                .canCancel(canCancelInvitation(invitation))
                .build();
    }

    // Utility methods

    private String getPhotoUrl(Long photoId) {
        if (photoId == null) {
            return null;
        }
        try {
            return fileUploadService.getFileUrl(photoId.intValue());
        } catch (Exception e) {
            logger.warn("Failed to get photo URL for ID: {}", photoId, e);
            return null;
        }
    }

    private List<String> getPhotoUrls(List<Long> photoIds) {
        if (photoIds == null || photoIds.isEmpty()) {
            return List.of();
        }
        return photoIds.stream()
                .map(this::getPhotoUrl)
                .filter(url -> url != null)
                .collect(Collectors.toList());
    }

    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private String formatMoney(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "KE"));
        return formatter.format(amount);
    }

    private String formatHeight(Double heightCm) {
        if (heightCm == null) {
            return null;
        }
        return String.format("%.2fm", heightCm / 100);
    }

    private String formatWeight(Double weightKg) {
        if (weightKg == null) {
            return null;
        }
        return String.format("%.0fkg", weightKg);
    }

    private String determinePlayerStatus(Player player, Optional<ClubMembership> currentMembership) {
        if (!player.getIsActive()) {
            return "Inactive";
        }
        
        if (currentMembership.isPresent()) {
            ClubMembership.MembershipStatus status = currentMembership.get().getMembershipStatus();
            switch (status) {
                case ACTIVE: return "Active";
                case INJURED: return "Injured";
                case SUSPENDED: return "Suspended";
                case ON_LOAN: return "On Loan";
                case TERMINATED: return "Contract Terminated";
                default: return "Unknown";
            }
        }
        
        return "Free Agent";
    }

    private boolean isAvailableForTransfer(Player player) {
        // Check if player has pending transfers
        return player.getTransfers().stream()
                .noneMatch(transfer -> transfer.getTransferStatus() == PlayerTransfer.TransferStatus.PENDING);
    }

    private String getDisplayName(Player player) {
        if (player.getNickname() != null && !player.getNickname().trim().isEmpty()) {
            return player.getNickname();
        }
        return player.getFirstName() + " " + player.getLastName();
    }

    private String determineAgeGroup(int age) {
        if (age < 18) return "Youth";
        if (age < 30) return "Senior";
        return "Veteran";
    }

    private String determineExperienceLevel(Player player) {
        int totalClubs = player.getClubMemberships().size();
        int totalTransfers = player.getTransfers().size();
        
        if (totalClubs == 0) return "Beginner";
        if (totalClubs <= 2 && totalTransfers <= 1) return "Developing";
        if (totalClubs <= 5 && totalTransfers <= 3) return "Experienced";
        return "Professional";
    }

    private String formatDuration(long durationInDays) {
        if (durationInDays <= 0) {
            return "0 days";
        }
        
        long years = durationInDays / 365;
        long months = (durationInDays % 365) / 30;
        long days = durationInDays % 30;
        
        StringBuilder duration = new StringBuilder();
        if (years > 0) duration.append(years).append(" year").append(years > 1 ? "s" : "");
        if (months > 0) {
            if (duration.length() > 0) duration.append(", ");
            duration.append(months).append(" month").append(months > 1 ? "s" : "");
        }
        if (days > 0 && years == 0) {
            if (duration.length() > 0) duration.append(", ");
            duration.append(days).append(" day").append(days > 1 ? "s" : "");
        }
        
        return duration.toString();
    }

    private boolean isContractExpiringSoon(LocalDate contractEndDate) {
        if (contractEndDate == null) {
            return false;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), contractEndDate) <= 180; // 6 months
    }

    private String getRoleDescription(ClubMembership membership) {
        if (membership.getIsCaptain()) {
            return "Captain";
        }
        if (membership.getIsViceCaptain()) {
            return "Vice Captain";
        }
        return "Player";
    }

    private String buildTransferDescription(PlayerTransfer transfer) {
        StringBuilder description = new StringBuilder();
        description.append(transfer.getTransferType().toString().replace("_", " ").toLowerCase());
        
        if (transfer.getFromClub() != null) {
            description.append(" from ").append(transfer.getFromClub().getName());
        }
        
        if (transfer.getToClub() != null) {
            description.append(" to ").append(transfer.getToClub().getName());
        }
        
        return description.toString();
    }

    private String getTransferStatusBadgeColor(PlayerTransfer.TransferStatus status) {
        switch (status) {
            case PENDING: return "warning";
            case COMPLETED: return "success";
            case CANCELLED: return "secondary";
            case FAILED: return "danger";
            default: return "primary";
        }
    }

    private String formatTimeUntilExpiry(long hoursUntilExpiry) {
        if (hoursUntilExpiry <= 0) {
            return "Expired";
        }
        
        long days = hoursUntilExpiry / 24;
        long hours = hoursUntilExpiry % 24;
        
        if (days > 0) {
            return days + " day" + (days > 1 ? "s" : "") + 
                   (hours > 0 ? ", " + hours + " hour" + (hours > 1 ? "s" : "") : "");
        }
        
        return hours + " hour" + (hours > 1 ? "s" : "");
    }

    private String getInvitationStatusBadgeColor(ClubInvitation.InvitationStatus status) {
        switch (status) {
            case PENDING: return "warning";
            case ACCEPTED: return "success";
            case REJECTED: return "danger";
            case EXPIRED: return "secondary";
            case CANCELLED: return "secondary";
            default: return "primary";
        }
    }

    private String buildInvitationSummary(ClubInvitation invitation) {
        StringBuilder summary = new StringBuilder();
        summary.append("Invited by ").append(invitation.getClub().getName());
        
        if (invitation.getProposedPosition() != null) {
            summary.append(" as ").append(invitation.getProposedPosition().toString().replace("_", " "));
        }
        
        return summary.toString();
    }

    private boolean canRespondToInvitation(ClubInvitation invitation) {
        return invitation.getStatus() == ClubInvitation.InvitationStatus.PENDING &&
               invitation.getExpiresAt().isAfter(LocalDateTime.now());
    }

    private boolean canCancelInvitation(ClubInvitation invitation) {
        return invitation.getStatus() == ClubInvitation.InvitationStatus.PENDING;
    }
}