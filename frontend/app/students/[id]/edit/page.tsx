"use client";

import { useEffect, useMemo, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

type StudentDetail = {
  student: Record<string, any>;
  studentCourseList: any[];
};

async function buildApiErrorMessage(res: Response) {
  const ct = res.headers.get("content-type") ?? "";

  if (ct.includes("application/json")) {
    const data: any = await res.json().catch(() => null);
    const base =
      (data?.message ? String(data.message) : "") ||
      `更新に失敗しました (HTTP ${res.status})`;

    const fieldErrors = Array.isArray(data?.fieldErrors) ? data.fieldErrors : [];

    if (fieldErrors.length > 0) {
      const lines = fieldErrors.map((e: any) => `- ${e.field}: ${e.message}`).join("\n");
      return `${base}\n${lines}`;
    }

    return `${base}\n${JSON.stringify(data, null, 2)}`;
  }

  const text = await res.text().catch(() => "");
  return text
    ? `更新に失敗しました (HTTP ${res.status})\n${text}`
    : `更新に失敗しました (HTTP ${res.status})`;
}

export default function StudentEditPage() {
  const params = useParams<{ id: string }>();
  const router = useRouter();
  const id = params?.id;

  const apiBase = useMemo(() => {
    return process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";
  }, []);

  const [detail, setDetail] = useState<StudentDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [loadError, setLoadError] = useState<string | null>(null);
  const [formError, setFormError] = useState<string | null>(null);


  const setStudentField = (key: string, value: any) => {
    setDetail((prev) => (prev ? { ...prev, student: { ...prev.student, [key]: value } } : prev));
  };

  // --- 取得 ---
  useEffect(() => {
    if (!id) return;

    (async () => {
      try {
        setLoading(true);
        setLoadError(null);

        const res = await fetch(`/api/student/${id}`, { cache: "no-store" });
        if (!res.ok) throw new Error(`詳細取得に失敗しました (HTTP ${res.status})`);

        const data = (await res.json()) as StudentDetail;
        if (!data?.student) throw new Error("APIレスポンスに student がありません");
        if (!Array.isArray(data.studentCourseList)) data.studentCourseList = [];

        setDetail(data);
      } catch (e: any) {
        setLoadError(e?.message ?? "不明なエラーです");
      } finally {
        setLoading(false);
      }
    })();
  }, [apiBase, id]);

  // --- 保存（PUT /updateStudent） ---
  const onSave = async () => {
    if (!detail) return;

    setFormError(null);

    const ageNum = Number(detail.student?.age);
    if (detail.student?.age === "" || detail.student?.age == null) {
      setFormError("年齢は必須です");
      return;
    }
    if (Number.isNaN(ageNum)) {
      setFormError("年齢は数値で入力してください");
      return;
    }

    try {
      setSaving(true);
      setFormError(null);

      const res = await fetch(`/api/updateStudent`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(detail),
      });

      if (!res.ok) {
        const msg = await buildApiErrorMessage(res);
        throw new Error(msg);
      }

      router.push(`/students/${id}`);
      router.refresh?.();
    } catch (e: any) {
      setFormError(e?.message ?? "不明なエラーです");
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="p-6">
        <Card>
          <CardHeader>
            <CardTitle>学生編集</CardTitle>
          </CardHeader>
          <CardContent>読み込み中…</CardContent>
        </Card>
      </div>
    );
  }

  if (loadError) {
    return (
      <div className="p-6 space-y-4">
        <Card>
          <CardHeader>
            <CardTitle>学生編集</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="whitespace-pre-wrap text-sm">{loadError}</div>
            <div className="flex gap-2">
              <Button variant="secondary" onClick={() => router.push(`/students/${id}`)}>
                詳細へ戻る
              </Button>
              <Button onClick={() => location.reload()}>再読み込み</Button>
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  if (!detail) {
    return (
      <div className="p-6">
        <Card>
          <CardHeader>
            <CardTitle>学生編集</CardTitle>
          </CardHeader>
          <CardContent>データがありません。</CardContent>
        </Card>
      </div>
    );
  }

  const student = detail.student;

  return (
    <div className="p-6 space-y-4">
      <Card>
        <CardHeader>
          <CardTitle>学生編集（ID: {id}）</CardTitle>
        </CardHeader>

        <CardContent className="space-y-6">
        {formError && (
          <div className="rounded-md border border-destructive/30 bg-destructive/10 p-3 text-sm whitespace-pre-wrap">
            {formError}
          </div>
        )}
          <div className="space-y-2">
            <Label htmlFor="name">氏名</Label>
            <Input
              id="name"
              value={(student?.name ?? "") as string}
              onChange={(e) => {
                setFormError(null);
                setStudentField("name", e.target.value);
              }}

            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="furigana">ふりがな</Label>
            <Input
              id="furigana"
              value={(student?.furigana ?? "") as string}
              onChange={(e) => {
                setFormError(null);
                setStudentField("furigana", e.target.value);
              }}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="email">メール</Label>
            <Input
              id="email"
              type="email"
              value={(student?.email ?? "") as string}
              onChange={(e) => {
                setFormError(null);
                setStudentField("email", e.target.value);
              }}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="city">居住地</Label>
            <Input
              id="city"
              value={(student?.city ?? "") as string}
              onChange={(e) => {
                setFormError(null);
                setStudentField("city", e.target.value);
              }}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="age">
              年齢 <span className="text-destructive">*</span>
            </Label>
            <Input
              id="age"
              type="number"
              inputMode="numeric"
              min={0}
              value={student?.age ?? ""}
              onChange={(e) => {
                setFormError(null);
                const v = e.target.value;
                setStudentField("age", v === "" ? "" : Number(v));
              }}
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="remark">備考</Label>
            <Input
              id="remark"
              value={(student?.remark ?? "") as string}
             onChange={(e) => {
               setFormError(null);
               setStudentField("remark", e.target.value);
             }}
            />
          </div>

          {/* コース編集 */}
          <div className="space-y-3">
            <div className="text-sm font-semibold">コース編集（コース名）</div>

            {detail.studentCourseList.length === 0 ? (
              <div className="text-sm text-muted-foreground">コース情報がありません</div>
            ) : (
              <div className="space-y-3">
                {detail.studentCourseList.map((c, idx) => (
                  <div key={String(c?.id ?? idx)} className="rounded-md border p-3 space-y-2">
                    <div className="text-xs text-muted-foreground">
                      courseId: {c?.id ?? "-"} / status: {c?.status ?? "-"}
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor={`courseName-${idx}`}>コース名</Label>
                      <Input
                        id={`courseName-${idx}`}
                        value={(c?.courseName ?? "") as string}
                        onChange={(e) => {
                          setFormError(null);
                          const next = detail.studentCourseList.slice();
                          next[idx] = { ...c, courseName: e.target.value };
                          setDetail({ ...detail, studentCourseList: next });
                        }}
                      />
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>


          <div className="flex gap-2">
            <Button variant="secondary" onClick={() => router.push(`/students/${id}`)} disabled={saving}>
              キャンセル
            </Button>
            <Button onClick={onSave} disabled={
                                       saving ||
                                       student?.age === "" ||
                                       student?.age == null ||
                                       Number.isNaN(Number(student?.age))
                                     }
>
              {saving ? "保存中…" : "保存"}
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
