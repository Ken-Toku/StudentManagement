import { Button } from "@/components/ui/button"
import { StudentTable, type StudentRow } from "@/components/student-table"
import Link from "next/link"

type StatusType = "仮申込" | "本申込" | "受講中" | "受講終了"

function pickDisplayCourse(
  studentCourseList: any[]
): { course: string; status: StatusType } {
  if (!Array.isArray(studentCourseList) || studentCourseList.length === 0) {
    return { course: "-", status: "仮申込" }
  }

  // 受講中があれば優先
  const inProgress = studentCourseList.find((c) => c?.status === "受講中")
  const picked = inProgress ?? studentCourseList[studentCourseList.length - 1]

  return {
    course: picked?.courseName ?? "-",
    status: (picked?.status ?? "仮申込") as StatusType,
  }
}

export default async function Page() {
  const res = await fetch(
    `${process.env.NEXT_PUBLIC_API_BASE_URL}/studentList`,
    { cache: "no-store" }
  )

  if (!res.ok) {
    return (
      <pre className="p-6">
        API error: {res.status} {res.statusText}
      </pre>
    )
  }

  const data = await res.json()
  const apiRows = Array.isArray(data) ? data : []

  const students: StudentRow[] = apiRows.map((r: any) => {
    const student = r?.student
    const { course, status } = pickDisplayCourse(r?.studentCourseList)

    return {
      id: String(student?.id ?? ""),
      name: student?.name ?? "",
      email: student?.email ?? "",
      course,
      status,
    }
  })

  return (
    <div className="min-h-screen bg-muted/30">
      <div className="container mx-auto py-8 px-4 md:px-8">
        <div className="mb-8 flex items-center justify-between">
          <h1 className="text-3xl font-bold tracking-tight text-foreground">
            学生一覧
          </h1>

          <Button asChild size="lg" className="font-semibold">
            <Link href="/students/new">新規登録</Link>
          </Button>
        </div>

        <StudentTable students={students} />
      </div>
    </div>
  )
}
