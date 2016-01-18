/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.os;

public final class Parcel {
    public final int readInt() {return 0;}
    public final String readString() {return "";}
    public final Parcelable readParcelable(ClassLoader loader) {return null;}
    public final Bundle readBundle() {return null;}
    public void writeInt(int i) {}
    public void writeString(String s) {}
    public void writeParcelable(Parcelable p) {}
    public void writeBundle(Bundle b) {}
}
