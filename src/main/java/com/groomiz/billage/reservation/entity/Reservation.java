package com.groomiz.billage.reservation.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "apply_date", nullable = false)
	private LocalDate applyDate;

	@Column(nullable = false)
	private Integer headcount;

	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalTime endTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReservationPurpose purpose;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classroom_id", nullable = false)
	private Classroom classroom;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_status_id", nullable = false)
	private ReservationStatus reservationStatus;
}