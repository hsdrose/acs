/*
===============================================================================
Copyright (c) 2014-2015, Samy Sadi. All rights reserved.
DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

This file is part of ACS - Advanced Cloud Simulator.

ACS is part of a research project undertaken by
Samy Sadi (samy.sadi.contact@gmail.com) and supervised by
Belabbas Yagoubi (byagoubi@gmail.com) in the
University of Oran1 Ahmed Benbella, Algeria.

ACS is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3
as published by the Free Software Foundation.

ACS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ACS. If not, see <http://www.gnu.org/licenses/>.
===============================================================================
*/

package com.samysadi.acs.service.staas.sfplacement;

import java.util.Iterator;
import java.util.List;

import com.samysadi.acs.core.entity.PoweredEntity.PowerState;
import com.samysadi.acs.hardware.Host;
import com.samysadi.acs.hardware.storage.Storage;
import com.samysadi.acs.hardware.storage.StorageFile;

/**
 * A placement policy that chooses the storage that has enough capacity for the storage file.<br/>
 * The storage is chosen according to the best fit method.
 * 
 * @author Samy Sadi <samy.sadi.contact@gmail.com>
 * @author Belabbas Yagoubi <byagoubi@gmail.com>
 * @since 1.0
 */
public class SfPlacementPolicyBestFit extends SfPlacementPolicyAbstract {

	public SfPlacementPolicyBestFit() {
		super();
	}

	protected boolean isStorageScoreBetter(double newScore, double compareToScore) {
		return Double.compare(newScore, compareToScore) < 0;
	}

	@Override
	protected Storage _selectStorage(StorageFile storageFile, List<Host> poweredOnHosts, List<Host> excludedHosts) {
		final int STORAGE_SEARCH_THRESHOLD = getConfigRec().getInt("SfPlacement.SearchThreshold", 20);

		int seenCount = 0;
		Storage bestStorage = null;
		double bestScore = 0.0d;

		Iterator<Host> it = poweredOnHosts.iterator();
		MAINLOOP:while (it.hasNext()) {
			final Host hostCandidate = it.next();
			if (excludedHosts != null && excludedHosts.contains(hostCandidate))
				continue;
			if (hostCandidate.getPowerState() == PowerState.ON) {
				Iterator<Storage> it2 = hostCandidate.getStorages().iterator();
				while (it2.hasNext()) {
					final Storage candidate = it2.next();
					final double s = computeStorageScore(storageFile, candidate);
					if (isStorageScoreBetter(s, bestScore)) {
						bestStorage = candidate;
						bestScore = s;
					}

					seenCount++;
					if (bestStorage != null && seenCount > STORAGE_SEARCH_THRESHOLD)
						break MAINLOOP;
				}
			}
		}
		return bestStorage;
	}
}
