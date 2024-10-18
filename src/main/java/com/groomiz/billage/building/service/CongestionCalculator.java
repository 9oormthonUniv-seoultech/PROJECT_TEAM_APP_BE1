package com.groomiz.billage.building.service;

import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomException;

public class CongestionCalculator {

	public static Congestion calculateCongestion(long totalReservations, long totalClassrooms) {
		if (totalClassrooms == 0) {
			throw new ClassroomException(ClassroomErrorCode.CLASSROOM_NOT_FOUND);
		}

		double ratio = (double) totalReservations / totalClassrooms;

		return Congestion.fromRatio(ratio);
	}
}
