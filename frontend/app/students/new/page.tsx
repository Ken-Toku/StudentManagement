"use client";

import { useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

type StatusType = "仮申込" | "本申込" | "受講中" | "受講終了";

type StudentCourse = {
  courseName: string;
  status: StatusType;
};

type Student = {
  name: string;
  furigana: string;
  age: number | "";
  gender: string;
  nickname: string;
  email: string;
  city: string;
  remark: string;
};

type StudentDetail = {
  student: Student;
  studentCourseList: StudentCourse[];
};

async function buildApiErrorMessage(res: Response) {
  const ct = res.headers.get("content-type") ?? "";
  if (ct.includes("application/json")) {
    const j = await res.json().catch(() => null);
    return j?.message ?? j?.error ?? JSON.stringify(j);
  }
  return await res.text().catch(() => "Unknown error");
}

export default function NewStudentPage() {
  const router = useRouter();
  const baseUrl = useMemo(
    () => process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080",
    []
  );

  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [student, setStudent] = useState<Student>({
    name: "",
    furigana: "",
    age: "",
    gender: "",
    nickname: "",
    email: "",
    city: "",
    remark: "",
  });

  const [courseName, setCourseName] = useState("");

  const onSave = async () => {
    setError(null);


    if (!student.name.trim()) return setError("氏名は必須です");
    if (student.age === "" || student.age == null) return setError("年齢は必須です");
    if (!student.city.trim()) return setError("都道府県は必須です");
    if (!courseName.trim()) return setError("コース名は必須です");

    const ageNum = Number(student.age);
    if (Number.isNaN(ageNum)) {
      return setError("年齢は数値で入力してください");
    }

    const payload: StudentDetail = {
      student: {
        ...student,
        age: ageNum,
      },
      studentCourseList: [{ courseName, status: "仮申込" }],
    };

    setSaving(true);
    try {
      const res = await fetch(`/api/registerStudent`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!res.ok) {
        const msg = await buildApiErrorMessage(res);
        throw new Error(msg);
      }

      // 登録後は一覧へ
      router.push("/students");
    } catch (e: any) {
      setError(e?.message ?? "登録に失敗しました");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="min-h-screen bg-muted/30">
      <div className="container mx-auto py-8 px-4 md:px-8 max-w-3xl">
        <Card>
          <CardHeader>
            <CardTitle>新規登録</CardTitle>
          </CardHeader>
          <CardContent className="space-y-6">
            {error && (
              <div className="rounded-md border border-destructive/30 bg-destructive/10 p-3 text-sm">
                {error}
              </div>
            )}

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <Label>氏名</Label>
                <Input value={student.name} onChange={(e) => setStudent({ ...student, name: e.target.value })} />
              </div>
              <div>
                <Label>フリガナ</Label>
                <Input
                  value={student.furigana}
                  onChange={(e) => setStudent({ ...student, furigana: e.target.value })}
                />
              </div>
              <div>
                <Label>年齢</Label>
                <Input
                  type="number"
                  value={student.age}
                  onChange={(e) => setStudent({ ...student, age: e.target.value === "" ? "" : Number(e.target.value) })}
                />
              </div>
              <div>
                <Label>性別</Label>
                <Input
                  value={student.gender}
                  onChange={(e) => setStudent({ ...student, gender: e.target.value })}
                />
              </div>
              <div>
                <Label>ニックネーム</Label>
                <Input
                  value={student.nickname}
                  onChange={(e) => setStudent({ ...student, nickname: e.target.value })}
                />
              </div>
              <div>
                <Label>メール</Label>
                <Input
                  value={student.email}
                  onChange={(e) => setStudent({ ...student, email: e.target.value })}
                />
              </div>
              <div>
                <Label>都道府県</Label>
                <Input value={student.city} onChange={(e) => setStudent({ ...student, city: e.target.value })} />
              </div>
              <div>
                <Label>備考</Label>
                <Input value={student.remark} onChange={(e) => setStudent({ ...student, remark: e.target.value })} />
              </div>
            </div>

            <div className="space-y-2">
              <Label>コース名（1件目）</Label>
              <Input value={courseName} onChange={(e) => setCourseName(e.target.value)} />
            </div>

            <div className="flex gap-2 justify-end">
              <Button variant="secondary" onClick={() => router.push("/students")} disabled={saving}>
                キャンセル
              </Button>
              <Button onClick={onSave} disabled={saving}>
                {saving ? "登録中…" : "登録"}
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
